package com.srs.tetris.bob.evaluator;

/**
 * Wraps an existing score and multiplies by a given weight.
 */
public class WeightedScore implements Score {
	private Score score;
	private double weight;

	public WeightedScore(Score score, double weight) {
		this.score = score;
		this.weight = weight;
	}

	@Override
	public double getScore() {
		return weight * score.getScore();
	}
}
