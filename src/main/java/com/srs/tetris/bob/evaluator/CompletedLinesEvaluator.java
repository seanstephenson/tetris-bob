package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.BitBoard;

/**
 * Counts the number of completed lines in a board.
 */
public class CompletedLinesEvaluator implements BoardEvaluator {
	@Override
	public Score evaluate(BitBoard board) {
		int completedLines = 0;
		for (int y = 0; y < board.getHeight(); y++) {
			if (board.isLineComplete(y)) {
				completedLines++;
			}
		}
		return new ScalarScore(completedLines);
	}
}
