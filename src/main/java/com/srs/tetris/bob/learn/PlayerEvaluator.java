package com.srs.tetris.bob.learn;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.player.DirectPlayer;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class PlayerEvaluator {

	private Supplier<DirectPlayer> playerSupplier;
	private int games;
	private List<Future<Game>> futures;
	private Consumer<Game> onGameStart;
	private ExecutorService executor;

	public PlayerEvaluator(Supplier<DirectPlayer> playerSupplier, ExecutorService executor, int games) {
		this.playerSupplier = playerSupplier;
		this.executor = executor;
		this.games = games;
	}

	public Result run() throws InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();

		futures = executor.invokeAll(Stream.generate(() -> new Callable<Game>() {
				@Override
				public Game call() throws Exception {
					// Create the game and notify the listener that a game is about to start.
					Game game = createGame();
					onGameStart.accept(game);

					// Play the game.
					game.run();

					return game;
				}
			})
			.limit(games)
			.collect(toList())
		);

		double elapsedTime = (System.currentTimeMillis() - start) / 1e3;

		return new Result(futures.stream().map(this::unwrapFuture).collect(toList()), elapsedTime);
	}

	private Game createGame() {
		DirectPlayer player = playerSupplier.get();

		GameSettings gameSettings = GameSettings.direct(player);
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

	public void setOnGameStart(Consumer<Game> onGameStart) {
		this.onGameStart = onGameStart;
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

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(threads);

		// Create an evaluator that runs a number of games.
		PlayerEvaluator evaluator = new PlayerEvaluator(
			() -> new BobPlayer(),
			executor,
			100
		);

		// Print progress on each game start.
		AtomicInteger count = new AtomicInteger(0);
		evaluator.setOnGameStart(game -> {
			System.out.print(".");
			if (count.incrementAndGet() % 100 == 0) System.out.println();
		});

		// Run the evaluation and get the result.
		Result result = evaluator.run();

		executor.shutdown();

		IntSummaryStatistics lines = result.getGames().stream()
			.mapToInt(g -> g.getCompletedLines())
			.summaryStatistics();

		IntSummaryStatistics pieces = result.getGames().stream()
			.mapToInt(g -> g.getTotalPieces())
			.summaryStatistics();

		LongSummaryStatistics score = result.getGames().stream()
			.mapToLong(g -> g.getScore())
			.summaryStatistics();

		DoubleSummaryStatistics elapsed = result.getGames().stream()
			.mapToDouble(g -> (g.getEndTime() - g.getStartTime()) / 1e3)
			.summaryStatistics();

		System.out.println();
		System.out.println();
		System.out.println("Lines: " + lines);
		System.out.println("Pieces: " + pieces);
		System.out.println("Score: " + score);
		System.out.println("Elapsed: " + elapsed);
		System.out.println();
		System.out.printf("Average Lines: %,.2f\n", lines.getAverage());
		System.out.printf("Total Elapsed Time (s): %,.3f\n", result.getElapsedTime());
		System.out.printf("Pieces per Second: %,.3f\n", pieces.getSum() / result.getElapsedTime());
	}
}
