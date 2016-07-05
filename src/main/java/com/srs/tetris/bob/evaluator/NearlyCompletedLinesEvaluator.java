package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.BitBoard;

/**
 * Counts the number of lines that only have one block remaining to be complete.
 */
public class NearlyCompletedLinesEvaluator implements BoardEvaluator {
	@Override
	public Score evaluate(BitBoard board) {
		int height = board.getHeight();
		int nearlyCompleteBlocks = board.getWidth() - 1;

		int lines = 0;

		for (int y = board.findHighestBlock(); y < height; y++) {
			if (board.countBlocksInLine(y) == nearlyCompleteBlocks) {
				lines++;
			}
		}

		return new ScalarScore(lines);
	}
}
