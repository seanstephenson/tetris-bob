package com.srs.tetris.web;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.bob.BobSettings;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.util.ThreadUtil;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Ensures that a single game is continually running.
 */
@Component
public class GameManager {
	private static final int POLL_DELAY = 1000;
	private ExecutorService gameExecutor = Executors.newCachedThreadPool();

	// The currently running game.
	private Game currentGame;

	// Supplier for game settings for automatically started games.
	@Autowired(required = false)
	private Supplier<GameSettings> gameSettingsSupplier;

	// Consumers to notify whenever a new game is created.
	@Autowired(required = false)
	private List<GameManagerListener> listeners;

	@PostConstruct
	public void init() {
		if (gameSettingsSupplier == null) {
			gameSettingsSupplier = () -> {
				BobPlayer player = new BobPlayer(new BobSettings().withSloppyMovement());
				return GameSettings.standard(player);
			};
		}

		// Start a game monitor thread that will continually run games.
		gameExecutor.submit(this::monitorGames);
	}

	@PreDestroy
	public void stop() {
		// Shut down the thread when the container tries to exit.
		gameExecutor.shutdownNow();
	}

	/**
	 * Starts a new game with settings from the game settings supplier.
	 */
	public void startNewGame() {
		startNewGame(gameSettingsSupplier.get());
	}

	/**
	 * Starts a new game with the given settings, cancelling the current game if there is one.
	 */
	public synchronized void startNewGame(GameSettings settings) {
		// End the current game.
		stopCurrentGame();

		// Create a new one with the given settings.
		Game game = new Game(settings);
		listeners.forEach(listener -> listener.onGameCreated(game));

		// Initialize and run it.
		game.init();
		gameExecutor.submit(game::run);

		// Keep a reference to it for callers.
		this.currentGame = game;
	}

	/**
	 * Ensures that a game is currently running, starting a new one if not.
	 */
	private synchronized void ensureGameRunning() {
		if (currentGame == null || currentGame.isGameOver()) {
			startNewGame();
		}
	}

	/**
	 * Stops the currently running game if there is one.
	 */
	public synchronized void stopCurrentGame() {
		if (currentGame != null) {
			currentGame.cancel();
			currentGame = null;
		}
	}

	private void monitorGames() {
		// Continually run games until the container exits and the executor interrupts us.
		while (!Thread.interrupted()) {
			// Make sure a game is currently running.
			ensureGameRunning();

			// Continually poll, waiting for the game to end.
			ThreadUtil.sleep(POLL_DELAY);
		}
	}

	public Game getCurrentGame() {
		return currentGame;
	}

	public interface GameManagerListener {
		/**
		 * Called whenever a new game is created, but before it has been initialized.  This provides an opportunity
		 * to add listeners to the game.
		 */
		void onGameCreated(Game game);
	}
}
