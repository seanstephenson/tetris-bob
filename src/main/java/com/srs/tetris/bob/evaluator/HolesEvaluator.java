package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;

/**
 * Counts any holes in the position, meaning any empty squares that have filled blocks on top of them.
 */
public class HolesEvaluator implements BoardEvaluator {
	@Override
	public Score evaluate(Board board) {
		// Look for holes in the board, meaning blocks that have an empty block underneath them.
		int holes = 0;

		for (int x = 0; x < board.getWidth(); x++) {
			int y = 0;

			// Move down from the top and find the first block.
			while (y < board.getHeight() && board.isEmpty(x, y)) {
				y++;
			}

			// Now find any empty blocks underneath it.
			while (y < board.getHeight()) {
				if (board.isEmpty(x, y)) {
					holes++;
				}
				y++;
			}
		}

		return new ScalarScore(holes);
	}
}
