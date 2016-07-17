package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;
import com.srs.tetris.game.BitBoard;

/**
 * Evaluates the position, incurring penalities for tall, narrow gaps which are hard to fill.
 */
public class NarrowGapEvaluator implements PositionEvaluator {

	private double twoGapWeight;
	private double threeGapWeight;
	private double fourGapWeight;

	public NarrowGapEvaluator(double twoGapWeight, double threeGapWeight, double fourGapWeight) {
		this.twoGapWeight = twoGapWeight;
		this.threeGapWeight = threeGapWeight;
		this.fourGapWeight = fourGapWeight;
	}

	@Override
	public Score evaluate(Position position) {
		BitBoard board = position.getBoard();
		int width = board.getWidth();
		int height = board.getHeight();
		int top = board.findHighestBlock();

		int twoGaps = 0;
		int threeGaps = 0;
		int fourGaps = 0;

		for (int x = 0; x < width; x++) {
			int gapHeight = 0;

			for (int y = height - 1; y >= top - 1; y--) {
				// Determine if this block is part of a narrow gap (surrounded on both sides).
				if (y >= 0 && board.isEmpty(x, y) && (x == 0 || !board.isEmpty(x - 1, y)) && (x == width - 1 || !board.isEmpty(x + 1, y))) {
					// Currently in a gap, so increment the height.
					gapHeight++;

				} else {
					if (gapHeight > 0) {
						// We were in a gap but not anymore, so increment this one.
						if (gapHeight == 2) {
							twoGaps++;
						} else if (gapHeight == 3) {
							threeGaps++;
						} else if (gapHeight >= 4) {
							fourGaps++;
						}
					}

					gapHeight = 0;
				}
			}
		}

		return new ScalarScore(
			twoGaps * twoGapWeight +
			threeGaps * threeGapWeight +
			fourGaps * fourGapWeight
		);
	}
}
