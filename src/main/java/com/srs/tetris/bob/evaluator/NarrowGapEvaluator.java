package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;

/**
 * Evaluates the position, incurring penalities for tall, narrow gaps which are hard to fill.
 */
public class NarrowGapEvaluator implements BoardEvaluator {

	private static final double TWO_GAP_WEIGHT = 0.5;
	private static final double THREE_GAP_WEIGHT = 1.0;
	private static final double FOUR_GAP_WEIGHT = 3.0;

	private static final double GAP_COUNT_EXPONENT = 2.0;

	@Override
	public NarrowGapScore evaluate(Board board) {
		// Calculate the height of each column.
		int[] heights = new int[board.getWidth()];
		for (int x = 0; x < board.getWidth(); x++) {
			heights[x] = calculateHeight(board, x);
		}

		// Now count the narrow gaps.
		int twoGaps = 0;
		int threeGaps = 0;
		int fourGaps = 0;

		for (int x = 0; x < board.getWidth(); x++) {
			int height = heights[x];

			int leftHeight = (x == 0) ? board.getHeight() : heights[x - 1];
			int rightHeight = (x == board.getWidth() - 1) ? board.getHeight() : heights[x + 1];

			if (height < leftHeight && height < rightHeight) {
				int gapHeight = Math.min(leftHeight, rightHeight) - height;

				if (gapHeight == 2) {
					twoGaps++;
				} else if (gapHeight == 3) {
					threeGaps++;
				} else if (gapHeight >= 4) {
					fourGaps++;
				}
			}
		}

		return new NarrowGapScore(twoGaps, threeGaps, fourGaps);
	}

	private int calculateHeight(Board board, int x) {
		for (int y = 0; y < board.getHeight(); y++) {
			if (!board.isEmpty(x, y)) {
				return board.getHeight() - y;
			}
		}
		return 0;
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
