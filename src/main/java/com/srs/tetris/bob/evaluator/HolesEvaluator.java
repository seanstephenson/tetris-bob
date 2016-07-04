package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;

/**
 * Counts any holes in the position, meaning any empty squares that have filled blocks on top of them.
 */
public class HolesEvaluator implements BoardEvaluator {

	private double holeWeight;
	private double coverWeight;

	public HolesEvaluator(double holeWeight, double coverWeight) {
		this.holeWeight = holeWeight;
		this.coverWeight = coverWeight;
	}

	private enum State { Solid, Above, Cover }

	@Override
	public HolesScore evaluate(Board board) {
		// Look for holes in the board, meaning blocks that have an empty block underneath them.
		int holes = 0;
		int covers = 0;

		// Ignore the completed lines.
		board = removeCompletedLines(board);

		for (int x = 0; x < board.getWidth(); x++) {
			int y = board.getHeight() - 1;

			State state = State.Solid;

			while (y >= 0) {
				boolean empty = board.isEmpty(x, y);

				if (state == State.Solid && empty) {
					// So far we haven't detected any holes and this space is empty.  We are now above the solid part.
					state = State.Above;

				} else if (state == State.Above && !empty) {
					// We were already above the solid part but found another filled space.  This is the cover above a hole.
					holes++;
					covers++;
					state = State.Cover;

				} else if (state == State.Cover && !empty) {
					// We are already in a cover and this is yet another cover.
					covers++;

				} else if (state == State.Cover && empty) {
					// We were in a cover, but now we are above it again.
					state = State.Above;
				}

				y--;
			}
		}

		return new HolesScore(holes, covers);
	}

	private Board removeCompletedLines(Board board) {
		Board originalBoard = board;

		for (int y = 0; y < board.getHeight(); y++) {
			if (board.isLineComplete(y)) {
				if (board == originalBoard) {
					board = board.clone();
				}
				board.removeLine(y);
			}
		}

		return board;
	}

	public class HolesScore implements Score {
		private int holes;
		private int covers;

		public HolesScore(int holes, int covers) {
			this.holes = holes;
			this.covers = covers;
		}

		public int getHoles() {
			return holes;
		}

		public int getCovers() {
			return covers;
		}

		@Override
		public double getScore() {
			return holes * holeWeight + covers * coverWeight;
		}
	}
}
