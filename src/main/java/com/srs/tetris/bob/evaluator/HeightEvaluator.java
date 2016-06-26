package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;

/**
 * Calculates the occupied height of the board, in lines.
 */
public class HeightEvaluator implements BoardEvaluator {
	@Override
	public Score evaluate(Board board) {
		for (int y = 0; y < board.getHeight(); y++) {
			if (!board.isLineEmpty(y)) {
				// A block was found at the given height.
				return new ScalarScore(board.getHeight() - y);
			}
		}

		// The board was empty.
		return new ScalarScore(0);
	}
}
