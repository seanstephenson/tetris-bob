package com.srs.tetris.player;

import com.srs.tetris.game.Input;
import com.srs.tetris.game.Piece;

/**
 * A player that uses "direct" input, telling the game exactly where the piece should go rather than
 * using normal input (left, right, down, etc).
 */
public interface DirectPlayer extends Player {
	/**
	 * Called to retrieve the location where the current piece should be placed.
	 */
	Piece directInput();

	/**
	 * Default implementation for direct players, sends no input.
	 */
	default Input input() {
		return new Input();
	}
}
