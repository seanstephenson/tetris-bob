package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;

/**
 * Wraps an existing evaluator, multiplying by a given weight.
 */
public class WeightedEvaluator implements BoardEvaluator {
	private BoardEvaluator evaluator;
	private double weight;

	public WeightedEvaluator(BoardEvaluator evaluator, double weight) {
		this.evaluator = evaluator;
		this.weight = weight;
	}

	@Override
	public Score evaluate(Board board) {
		return new WeightedScore(evaluator.evaluate(board), weight);
	}
}