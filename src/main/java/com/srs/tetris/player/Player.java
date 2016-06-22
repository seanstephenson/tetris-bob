package com.srs.tetris.player;

import com.srs.tetris.game.Input;
import com.srs.tetris.game.Game;

/**
 * A Tetris game player.
 */
public interface Player {
	/**
	 * Called by the game thread before the game starts.
	 */
	default void init(Game game) {}

	/**
	 * Called on each frame in order to get the input for this player.
	 */
	public Input input();
}
