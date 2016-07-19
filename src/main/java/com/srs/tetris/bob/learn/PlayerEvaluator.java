package com.srs.tetris.bob.learn;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.bob.BobSettings;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.player.DirectPlayer;
import com.srs.tetris.replay.Replay;
import com.srs.tetris.replay.ReplayUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class PlayerEvaluator {

	private Supplier<DirectPlayer> playerSupplier;
	private ExecutorService executor;

	private int gameCount;
	private int maxLinesPerGame;

	private Consumer<Game> onGameEnd = (game) -> {};

	private boolean generateReplays;

	public PlayerEvaluator(Supplier<DirectPlayer> playerSupplier, ExecutorService executor, int gameCount) {
		this.playerSupplier = playerSupplier;
		this.executor = executor;
		this.gameCount = gameCount;
	}

	public Result run() {
		try {
			long start = System.currentTimeMillis();

			// Run the games.
			List<Game> games = runGames();

			double elapsedTime = (System.currentTimeMillis() - start) / 1e3;
			return new Result(games, elapsedTime);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Game> runGames() throws InterruptedException {
		List<Future<Game>> futures = executor.invokeAll(Stream.generate(() -> new Callable<Game>() {
				@Override
				public Game call() throws Exception {
					// Create and play the game.
					Game game = createGame();

					game.run();
					onGameEnd.accept(game);

					return game;
				}
			})
			.limit(gameCount)
			.collect(toList())
		);

		return futures.stream().map(this::unwrapFuture).collect(toList());
	}

	private Game createGame() {
		DirectPlayer player = playerSupplier.get();

		GameSettings gameSettings = GameSettings.direct(player);
		gameSettings.setGenerateReplay(generateReplays);
		gameSettings.setMaxLines(maxLinesPerGame);

		Game game = new Game(gameSettings);
		game.init();

		return game;
	}

	private <T> T unwrapFuture(Future<T> future) {
		try {
			return future.get();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public void setOnGameEnd(Consumer<Game> onGameEnd) {
		this.onGameEnd = onGameEnd;
	}

	public void setGenerateReplays(boolean generateReplays) {
		this.generateReplays = generateReplays;
	}

	public void setMaxLinesPerGame(int maxLinesPerGame) {
		this.maxLinesPerGame = maxLinesPerGame;
	}

	public static class Result {
		private List<Game> games;
		private double elapsedTime;

		public Result(List<Game> games, double elapsedTime) {
			this.games = games;
			this.elapsedTime = elapsedTime;
		}

		public List<Game> getGames() {
			return games;
		}

		public double getElapsedTime() {
			return elapsedTime;
		}
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(threads);

		// Create an evaluator that runs a number of games.
		PlayerEvaluator evaluator = new PlayerEvaluator(
			() -> new BobPlayer(BobSettings.simple()),
			//() -> new BobPlayer(BobSettings.noSwap().setMaxDepth(2)),
			//() -> new BobPlayer(BobSettings.noSwap().setMaxDepth(4)),
			executor,
			100
		);

		// Generate replays.
		evaluator.setGenerateReplays(false);
		//evaluator.setGenerateReplays(true);

		evaluator.setMaxLinesPerGame(-1);
		//evaluator.setMaxLinesPerGame(100);

		// Print progress on each game start.
		evaluator.setOnGameEnd(new PrintDotConsumer<Game>());

		// Run the evaluation and get the result.
		Result result = evaluator.run();

		executor.shutdown();

		LongSummaryStatistics lines = result.getGames().stream()
			.mapToLong(Game::getCompletedLines)
			.summaryStatistics();

		LongSummaryStatistics pieces = result.getGames().stream()
			.mapToLong(Game::getTotalPieces)
			.summaryStatistics();

		LongSummaryStatistics score = result.getGames().stream()
			.mapToLong(Game::getScore)
			.summaryStatistics();

		DoubleSummaryStatistics elapsed = result.getGames().stream()
			.mapToDouble(g -> (g.getStartTime().until(g.getEndTime(), ChronoUnit.NANOS) / 1e9))
			.summaryStatistics();

		LongSummaryStatistics positions = result.getGames().stream()
			.map(game -> (BobPlayer) game.getPlayer())
			.mapToLong(BobPlayer::getPositionsEvaluated)
			.summaryStatistics();

		// If we were recording replays, store the replay for the worst 5 games.
		if (evaluator.generateReplays) {
			List<Replay> replays = result.getGames().stream()
				.sorted(Comparator.comparing(Game::getCompletedLines))
				.limit(5)
				.map(Game::getReplay)
				.collect(Collectors.toList());

			Path outputBase = FileUtil.getReplayDataBase().resolve(FileUtil.createFilenameSafeTimestamp());
			Files.createDirectories(outputBase);

			for (int i = 0; i < replays.size(); i++) {
				Replay replay = replays.get(i);
				ReplayUtil.writeReplay(replay, outputBase.resolve(String.format("replay.%d", i + 1)));
			}
		}

		System.out.println();
		System.out.println();
		System.out.println("Lines: " + lines);
		System.out.println("Pieces: " + pieces);
		System.out.println("Score: " + score);
		System.out.println("Elapsed: " + elapsed);
		System.out.println();
		System.out.printf("Average Lines: %,.2f\n", lines.getAverage());
		System.out.println();
		System.out.printf("Positions per Piece: %,.3f\n", (double) positions.getSum() / pieces.getSum());
		System.out.printf("Positions per Second: %,.3f\n", positions.getSum() / result.getElapsedTime());
		System.out.printf("Pieces per Second: %,.3f\n", pieces.getSum() / result.getElapsedTime());
		System.out.printf("Total Elapsed Time (s): %,.3f\n", result.getElapsedTime());
	}
}
