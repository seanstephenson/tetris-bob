package com.srs.tetris.game;

/**
 * A strategy for generating pieces
 */
public interface PieceGenerator {
	/**
	 * Generates the next piece to be used in the game.
	 */
	PieceType generate();
}
