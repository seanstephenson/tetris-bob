package com.srs.tetris.bob;

import com.srs.tetris.game.Board;

/**
 * Generates a score indicating the favorability of a given board position.
 */
public class BoardEvaluator {
	public Score evaluate(Board board) {
		int height = computeHeight(board);
		return new Score(-height);
	}

	public int computeHeight(Board board) {
		for (int y = 0; y < board.getHeight(); y++) {
			if (!board.isLineEmpty(y)) {
				// A block was found at the given height.
				return board.getHeight() - y;
			}
		}

		// The board was empty.
		return 0;
	}
}
