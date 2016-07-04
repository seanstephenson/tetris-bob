package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.BoardEvaluator;
import com.srs.tetris.bob.evaluator.SapientEvaluator;
import com.srs.tetris.game.Board;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.Piece;
import java.util.ArrayList;
import java.util.List;

public class MoveSelector {
	private Board board;
	private Piece piece;

	//private Piece nextPiece, swapPiece;

	public MoveSelector(Game game) {
		this.board = game.getBoard();
		this.piece = game.getPiece();
		//this.nextPiece = game.getNextPiece();
		//this.swapPiece = game.getSwapPiece();
	}

	public Move getMove() {
		BoardEvaluator evaluator = new SapientEvaluator();

		Board originalBoard = this.board.clone();
		Board board = originalBoard;

		// Enumerate all the current moves.
		List<Move> moves = findPossibleMoves(board);

		Move best = null;
		for (Move move : moves) {
			Piece piece = this.piece.moveTo(move.getX(), move.getY(), move.getOrientation());

			// Draw the piece on the board so we can see what it would look like after.
			board = doMove(board, piece);

			// Evaluate the position.
			move.setScore(evaluator.evaluate(board));

			// Now undo the move.
			board = originalBoard;
			board.remove(piece);

			// If this move is the best so far, remember it.
			if (best == null || move.getScore().getScore() > best.getScore().getScore()) {
				best = move;
			}
		}

		return best;
	}

	private Board doMove(Board board, Piece piece) {
		// First, place the piece on the board.
		board.place(piece);

		// Now, remove completed lines, checking only the lines that were impacted by the piece.
		Board result = board;

		int top = Math.max(0, piece.getY());
		int bottom = Math.min(board.getHeight(), top + piece.getBoard().getHeight());
		for (int y = top; y < bottom; y++) {
			if (result.isLineComplete(y)) {
				// This line was complete.  Clone the board if it hasn't been cloned yet and remove it.
				if (result == board) {
					result = board.clone();
				}
				result.removeLine(y);
			}
		}

		return result;
	}

	private ArrayList<Move> findPossibleMoves(Board board) {
		ArrayList<Move> moves = new ArrayList<>(board.getWidth() * 4);

		// For each possible orientation.
		for (int orientation = 0; orientation < 4; orientation++) {
			Piece piece = this.piece.moveTo(0, 0, orientation);

			// For each possible horizontal position.
			for (int x = -piece.getBoard().getWidth(); x < board.getWidth(); x++) {
				piece = piece.moveTo(x, 0);
				if (!board.canPlace(piece)) continue;

				// Drop the piece until it lands.
				while (board.canPlace(piece.moveDown())) {
					piece = piece.moveDown();
				}

				moves.add(new Move(piece));
			}
		}

		return moves;
	}
}
