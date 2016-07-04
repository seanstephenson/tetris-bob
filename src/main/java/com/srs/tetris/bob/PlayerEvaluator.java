package com.srs.tetris.bob;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.player.Player;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

		ExecutorService executor = Executors.newFixedThreadPool(threads);

		futures = executor.invokeAll(IntStream.range(0, games)
			.mapToObj(i -> new Callable<Game>() {
				@Override
				public Game call() throws Exception {
					System.out.println("Starting game " + i);
					return createAndPlayGame();
				}
			})
			.collect(toList())
		);

		double averageLines = futures.stream().map(this::unwrapFuture)
			.mapToInt(g -> g.getCompletedLines())
			.average().orElse(0.0);

		double averageScore = futures.stream().map(this::unwrapFuture)
			.mapToInt(g -> g.getScore())
			.average().orElse(0.0);

		double averageElapsedTime = futures.stream().map(this::unwrapFuture)
			.mapToDouble(g -> g.getEndTime() - g.getStartTime())
			.average().orElse(0.0);

		System.out.println();
		System.out.printf("Average Lines: %.2f\n", averageLines);
		System.out.printf("Average Score: %,.2f\n", averageScore);
		System.out.printf("Average Elapsed Time (ms): %,.2f\n", averageElapsedTime);
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
