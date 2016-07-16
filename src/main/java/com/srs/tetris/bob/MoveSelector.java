package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.BoardEvaluator;
import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Piece;
import java.util.List;

public class MoveSelector {

	private MoveEnumerator moveEnumerator;
	private BoardEvaluator evaluator;

	public MoveSelector(MoveEnumerator moveEnumerator, BoardEvaluator evaluator) {
		this.moveEnumerator = moveEnumerator;
		this.evaluator = evaluator;
	}

	public Move getMove(Position position) {
		// Enumerate all the current moves.
		List<Move> moves = moveEnumerator.findPossibleMoves(position);

		Move best = null;
		for (Move move : moves) {
			BitBoard board = position.getBoard().clone();

			// Draw the piece on the board so we can see what it would look like after.
			doMove(board, move.getPiece());

			// Evaluate the position.
			move.setScore(evaluator.evaluate(board));

			// If this move is the best so far, remember it.
			if (best == null || move.getScore().getScore() > best.getScore().getScore()) {
				best = move;
			}
		}

		return best;
	}

	private void doMove(BitBoard board, Piece piece) {
		// First, place the piece on the board.
		board.place(piece);

		// Now, remove completed lines, checking only the lines that were impacted by the piece.
		int top = Math.max(0, piece.getY());
		int bottom = Math.min(board.getHeight(), top + piece.getBoard().getHeight());

		for (int y = top; y < bottom; y++) {
			if (board.isLineComplete(y)) {
				board.removeLine(y);
			}
		}
	}
}
