package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.BitBoard;

/**
 * Calculates the occupied height of the board, in lines.
 */
public class HeightEvaluator implements BoardEvaluator {
	@Override
	public Score evaluate(BitBoard board) {
		return new ScalarScore(board.getHeight() - board.findHighestBlock());
	}
}
