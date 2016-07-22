package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;
import com.srs.tetris.game.BitBoard;

/**
 * Gives a high
 */
public class DangerZoneEvaluator implements PositionEvaluator {
	private int dangerZoneSize;
	private double exponent;

	public DangerZoneEvaluator(int dangerZoneSize, double exponent) {
		this.dangerZoneSize = dangerZoneSize;
		this.exponent = exponent;
	}

	@Override
	public Score evaluate(Position position) {
		BitBoard board = position.getBoard();
		int height = 0;
		int dangerZoneSize = Math.min(board.getHeight(), this.dangerZoneSize);

		for (int y = 0; y < dangerZoneSize; y++) {
			if (!board.isLineEmpty(y)) {
				height = dangerZoneSize - y;
				break;
			}
		}

		return new ScalarScore(Math.pow(height, exponent));
	}
}
