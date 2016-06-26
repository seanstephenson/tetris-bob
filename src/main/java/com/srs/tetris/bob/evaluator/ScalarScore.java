package com.srs.tetris.bob.evaluator;

/**
 * A single value score.
 */
public class ScalarScore implements Score {

	private double score;

	public ScalarScore(double score) {
		this.score = score;
	}

	@Override
	public double getScore() {
		return score;
	}
}
