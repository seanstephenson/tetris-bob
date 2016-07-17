package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;

/**
 * Generates a score indicating the favorability of a given board position.
 */
public interface PositionEvaluator {
	/**
	 * Evaluates the given board and returns a score.
	 */
	Score evaluate(Position position);
}
