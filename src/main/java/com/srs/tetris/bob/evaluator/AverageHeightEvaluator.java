package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;
import com.srs.tetris.game.BitBoard;

/**
 * Calculates the average height (center of mass on the y axis) of the board in lines.
 */
public class AverageHeightEvaluator implements PositionEvaluator {
	@Override
	public Score evaluate(Position position) {
		BitBoard board = position.getBoard();
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
