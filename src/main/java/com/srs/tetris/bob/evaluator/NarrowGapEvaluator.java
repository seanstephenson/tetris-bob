package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.BitBoard;

/**
 * Evaluates the position, incurring penalities for tall, narrow gaps which are hard to fill.
 */
public class NarrowGapEvaluator implements BoardEvaluator {

	private static final double TWO_GAP_WEIGHT = 0.5;
	private static final double THREE_GAP_WEIGHT = 1.0;
	private static final double FOUR_GAP_WEIGHT = 3.0;

	private static final double GAP_COUNT_EXPONENT = 1.0;

	@Override
	public NarrowGapScore evaluate(BitBoard board) {
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

		return new NarrowGapScore(twoGaps, threeGaps, fourGaps);
	}

	public static class NarrowGapScore implements Score {
		private int twoGaps;
		private int threeGaps;
		private int fourGaps;

		public NarrowGapScore(int twoGaps, int threeGaps, int fourGaps) {
			this.twoGaps = twoGaps;
			this.threeGaps = threeGaps;
			this.fourGaps = fourGaps;
		}

		@Override
		public double getScore() {
			// Penalize large numbers of gaps more harshly by raising each gap count to a positive power, then weight them.
			// (i.e. one gap four deep is mostly ok, two is bad, but three or more is almost catastrophic)
			return Math.pow(twoGaps, GAP_COUNT_EXPONENT) * TWO_GAP_WEIGHT +
				Math.pow(threeGaps, GAP_COUNT_EXPONENT) * THREE_GAP_WEIGHT +
				Math.pow(fourGaps, GAP_COUNT_EXPONENT) * FOUR_GAP_WEIGHT;
		}

		public int getTwoGaps() {
			return twoGaps;
		}

		public int getThreeGaps() {
			return threeGaps;
		}

		public int getFourGaps() {
			return fourGaps;
		}
	}
}
