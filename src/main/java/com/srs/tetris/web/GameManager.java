package com.srs.tetris.web;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.bob.BobSettings;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameSettings;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Ensures that a single game is continually running.
 */
@Component
public class GameManager {
	private ExecutorService gameExecutor = Executors.newCachedThreadPool();

	private Game game;

	@PostConstruct
	public void startGame() {
		BobPlayer player = new BobPlayer(new BobSettings().withSloppyMovement());
		GameSettings settings = GameSettings.standard(player);
		game = new Game(settings);
		game.init();
		gameExecutor.submit(() -> game.run());
	}

	public Game getGame() {
		return game;
	}
}
