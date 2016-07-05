package com.srs.tetris.bob;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.player.Player;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class PlayerEvaluator {

	private int threads = Runtime.getRuntime().availableProcessors();

	private int games;
	private List<Future<Game>> futures;

	public PlayerEvaluator(int games) {
		this.games = games;
	}

	public void evaluate() throws InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();

		ExecutorService executor = Executors.newFixedThreadPool(threads);

		AtomicInteger count = new AtomicInteger(0);
		futures = executor.invokeAll(Stream.generate(() -> new Callable<Game>() {
				@Override
				public Game call() throws Exception {
					System.out.print(".");
					if (count.incrementAndGet() % 100 == 0) System.out.println();

					return createAndPlayGame();
				}
			})
			.limit(games)
			.collect(toList())
		);

		executor.shutdown();

		IntSummaryStatistics lines = futures.stream().map(this::unwrapFuture)
			.mapToInt(g -> g.getCompletedLines())
			.summaryStatistics();

		IntSummaryStatistics pieces = futures.stream().map(this::unwrapFuture)
			.mapToInt(g -> g.getTotalPieces())
			.summaryStatistics();

		IntSummaryStatistics score = futures.stream().map(this::unwrapFuture)
			.mapToInt(g -> g.getScore())
			.summaryStatistics();

		DoubleSummaryStatistics elapsed = futures.stream().map(this::unwrapFuture)
			.mapToDouble(g -> (g.getEndTime() - g.getStartTime()) / 1e3)
			.summaryStatistics();


		double totalElapsed = (System.currentTimeMillis() - start) / 1e3;

		System.out.println();
		System.out.println();
		System.out.println("Lines: " + lines);
		System.out.println("Pieces: " + pieces);
		System.out.println("Score: " + score);
		System.out.println("Elapsed: " + elapsed);
		System.out.println();
		System.out.printf("Average Lines: %,.2f\n", lines.getAverage());
		System.out.printf("Total Elapsed Time (s): %,.3f\n", totalElapsed);
		System.out.printf("Pieces per Second: %,.3f\n", pieces.getSum() / totalElapsed);
	}

	private Game createAndPlayGame() {
		Game game = createGame();
		game.run();
		return game;
	}

	private Game createGame() {
		BobPlayer player = new BobPlayer();

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

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		PlayerEvaluator evaluator = new PlayerEvaluator(1000);
		evaluator.evaluate();
	}
}
