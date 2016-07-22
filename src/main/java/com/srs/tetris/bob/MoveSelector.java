package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.PositionEvaluator;
import com.srs.tetris.bob.evaluator.ScalarScore;
import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Piece;
import java.util.List;

public class MoveSelector {

	private MoveEnumerator moveEnumerator;
	private PositionEvaluator evaluator;

	public MoveSelector(MoveEnumerator moveEnumerator, PositionEvaluator evaluator) {
		this.moveEnumerator = moveEnumerator;
		this.evaluator = evaluator;
	}

	/**
	 * Selects the best move to make in the given position.
	 *
	 * @param position the position for which to find a move.
	 * @param depth the maximum depth to search for next pieces.
	 *
	 * @return the best move.
	 */
	public Move getMove(Position position, int depth) {
		// Enumerate all the current moves.
		List<Move> moves = moveEnumerator.findPossibleMoves(position);

		Move best = null;
		for (Move move : moves) {
			// Calculate the position after this move.
			Position after = position.doMove(move);

			if (depth == 1 || after.getPiece() == null) {
				// This is the bottom of the move tree, so evaluate the position.
				move.setScore(evaluator.evaluate(after));

			} else {
				// We can still go deeper, so find the best move from this position.
				int nextDepth = move.isSwap() ? depth : depth - 1;
				Move nextMove = getMove(after, nextDepth);

				if (nextMove != null) {
					// Pass the next move's best score onto this move.
					move.setScore(nextMove.getScore());

				} else {
					// There were no possible moves from this position, so it's game over!
					move.setScore(new ScalarScore(Double.NEGATIVE_INFINITY));
				}
			}

			// If this move is the best so far, remember it.
			if (best == null || move.getScore().getScore() > best.getScore().getScore()) {
				best = move;
			}
		}

		return best;
	}
}
