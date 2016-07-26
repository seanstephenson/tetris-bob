package com.srs.tetris.bob;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.Input;

public interface InputSupplier {
	/**
	 * Called on each frame when input is requested.  Creates the input to perform the given move.
	 *
	 * @param move the move to perform, or null if no move has been found yet.
	 * @param game the game for which to perform the move.
	 */
	Input createInput(Move move, Game game);
}
