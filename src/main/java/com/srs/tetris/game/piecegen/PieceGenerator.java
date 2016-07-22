package com.srs.tetris.game.piecegen;

import com.srs.tetris.game.PieceType;

/**
 * A strategy for generating pieces
 */
public interface PieceGenerator {
	/**
	 * Generates the next piece to be used in the game.
	 */
	PieceType generate();
}
