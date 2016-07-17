package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.PositionEvaluator;
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

	public Move getMove(Position position) {
		// Enumerate all the current moves.
		List<Move> moves = moveEnumerator.findPossibleMoves(position);

		Move best = null;
		for (Move move : moves) {
			// Draw the piece on the board so we can see what it would look like after.
			Position after = doMove(position, move);

			// Evaluate the position.
			move.setScore(evaluator.evaluate(after));

			// If this move is the best so far, remember it.
			if (best == null || move.getScore().getScore() > best.getScore().getScore()) {
				best = move;
			}
		}

		return best;
	}

	private Position doMove(Position position, Move move) {
		// First, place the piece on the board.
		BitBoard board = position.getBoard().clone();
		Piece piece = move.getPiece();
		board.place(piece);

		// Now, remove completed lines, checking only the lines that were impacted by the piece.
		int top = Math.max(0, piece.getY());
		int bottom = Math.min(board.getHeight(), top + piece.getBoard().getHeight());

		for (int y = top; y < bottom; y++) {
			if (board.isLineComplete(y)) {
				board.removeLine(y);
			}
		}

		if (position.getPiece().getType() == move.getPiece().getType()) {
			// This was a normal move (not a swap).
			return new Position(board, position.getNextPiece(), null, position.getSwapPiece(), position.isPieceSwapped());

		} else {
			// This was a swap move.
			Piece nextPiece = position.getNextPiece();
			piece = position.getSwapPiece();

			if (piece == null) {
				// There was no swap piece, so use the next piece.
				piece = nextPiece;
				nextPiece = null;
			}

			return new Position(board, piece, nextPiece, position.getPiece(), true);
		}
	}
}
