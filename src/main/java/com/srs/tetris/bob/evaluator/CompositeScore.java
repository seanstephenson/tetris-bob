package com.srs.tetris.bob.evaluator;

public class CompositeScore implements Score {
	private Score[] scores;

	public CompositeScore(Score[] scores) {
		this.scores = scores;
	}

	@Override
	public double getScore() {
		double total = 0.0;
		for (Score score : scores) {
			total += score.getScore();
		}
		return total;
	}
}
