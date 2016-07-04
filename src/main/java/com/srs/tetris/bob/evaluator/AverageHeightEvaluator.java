package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;

/**
 * Calculates the average height (center of mass on the y axis) of the board in lines.
 */
public class AverageHeightEvaluator implements BoardEvaluator {
	@Override
	public Score evaluate(Board board) {
		double totalHeight = 0;
		int count = 0;

		for (int y = board.findHighestBlock(); y < board.getHeight(); y++) {
			int lineCount = board.countBlocksInLine(y);
			totalHeight += lineCount * (board.getHeight() - y);
			count += lineCount;
		}

		return new ScalarScore(count > 0 ? totalHeight / count : 0);
	}
}
