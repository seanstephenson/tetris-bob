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

		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				if (!board.isEmpty(x, y)) {
					totalHeight += board.getHeight() - y;
					count++;
				}
			}
		}

		return new ScalarScore(count > 0 ? totalHeight / count : 0);
	}
}
