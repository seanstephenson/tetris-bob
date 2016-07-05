package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Board;

/**
 * Counts any holes in the position, meaning any empty squares that have filled blocks on top of them.
 */
public class HolesEvaluator implements BoardEvaluator {

	@Override
	public Score evaluate(BitBoard board) {
		int width = board.getWidth();
		int height = board.getHeight();
		int top = board.findHighestBlock();

		// Walk through the board and count transitions from a 1 to a 0 and back.
		int transitions = 0;

		// Start with a completely empty line at the top.
		int previous = 0;

		for (int y = top; y < height; y++) {
			int line = board.getLine(y);

			// Count the number of transitions.
			transitions += Integer.bitCount(line ^ previous);
			previous = line;
		}

		// Now count the final transition to a full line.
		transitions += Integer.bitCount(board.getLineMask() ^ previous);

		// There will always be one transition per column, and then two on top of that for each hole.
		int holes = (transitions - width) / 2;

		return new ScalarScore(holes);
	}
}
