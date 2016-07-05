package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.BitBoard;

/**
 * Generates a score indicating the favorability of a given board position.
 */
public interface BoardEvaluator {
	/**
	 * Evaluates the given board and returns a score.
	 */
	Score evaluate(BitBoard board);
}
