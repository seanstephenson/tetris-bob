package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;

/**
 * Wraps an existing evaluator, multiplying by a given weight.
 */
public class WeightedEvaluator implements PositionEvaluator {
	private PositionEvaluator evaluator;
	private double weight;

	public WeightedEvaluator(PositionEvaluator evaluator, double weight) {
		this.evaluator = evaluator;
		this.weight = weight;
	}

	@Override
	public Score evaluate(Position position) {
		return new WeightedScore(evaluator.evaluate(position), weight);
	}
}
