package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;
import com.srs.tetris.game.BitBoard;

/**
 * Calculates the occupied height of the board, in lines.
 */
public class HeightEvaluator implements PositionEvaluator {
	@Override
	public Score evaluate(Position position) {
		BitBoard board = position.getBoard();
		return new ScalarScore(board.getHeight() - board.findHighestBlock());
	}
}
