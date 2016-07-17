package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;
import com.srs.tetris.game.BitBoard;

/**
 * Counts the number of lines that only have one block remaining to be complete.
 */
public class NearlyCompletedLinesEvaluator implements PositionEvaluator {
	@Override
	public Score evaluate(Position position) {
		BitBoard board = position.getBoard();
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
