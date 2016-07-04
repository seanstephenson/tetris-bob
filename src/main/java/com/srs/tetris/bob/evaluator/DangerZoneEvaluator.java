package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;

/**
 * Gives a high
 */
public class DangerZoneEvaluator implements BoardEvaluator {
	private int dangerZoneSize;
	private double exponent;

	public DangerZoneEvaluator(int dangerZoneSize, double exponent) {
		this.dangerZoneSize = dangerZoneSize;
		this.exponent = exponent;
	}

	@Override
	public Score evaluate(Board board) {
		int height = 0;

		for (int y = 0; y < dangerZoneSize; y++) {
			if (!board.isLineEmpty(y)) {
				height = dangerZoneSize - y;
				break;
			}
		}

		return new ScalarScore(Math.pow(height, exponent));
	}
}
