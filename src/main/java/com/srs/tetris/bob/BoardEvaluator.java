package com.srs.tetris.bob;

import com.srs.tetris.game.Board;

/**
 * Generates a score indicating the favorability of a given board position.
 */
public class BoardEvaluator {
	// Overall
	private static final double HEIGHT_WEIGHT = -1.0;
	private static final double HOLE_WEIGHT = -10.0;
	private static final double COMPLETED_LINES_WEIGHT = 2.0;

	public Score evaluate(Board board) {
		int height = computeHeight(board);
		int holes = computeHoles(board);
		int completedLines = computeCompletedLines(board);

		return new Score(
			height * HEIGHT_WEIGHT +
			holes * HOLE_WEIGHT +
			completedLines * COMPLETED_LINES_WEIGHT
		);
	}

	public int computeHeight(Board board) {
		for (int y = 0; y < board.getHeight(); y++) {
			if (!board.isLineEmpty(y)) {
				// A block was found at the given height.
				return board.getHeight() - y;
			}
		}

		// The board was empty.
		return 0;
	}

	private int computeHoles(Board board) {
		// Look for holes in the board, meaning blocks that have an empty block underneath them.
		int holes = 0;

		for (int x = 0; x < board.getWidth(); x++) {
			int y = 0;

			// Move down from the top and find the first block.
			while (y < board.getHeight() && board.isEmpty(x, y)) {
				y++;
			}

			// Now find any empty blocks underneath it.
			while (y < board.getHeight()) {
				if (board.isEmpty(x, y)) {
					holes++;
				}
				y++;
			}
		}

		return holes;
	}

	private int computeCompletedLines(Board board) {
		int completedLines = 0;
		for (int y = 0; y < board.getHeight(); y++) {
			if (board.isLineComplete(y)) {
				completedLines++;
			}
		}
		return completedLines;
	}
}
