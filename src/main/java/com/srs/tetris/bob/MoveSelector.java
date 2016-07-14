package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.BoardEvaluator;
import com.srs.tetris.bob.evaluator.SapientEvaluator;
import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.Piece;
import java.util.ArrayList;
import java.util.List;

public class MoveSelector {
	private BitBoard board;
	private Piece piece;

	//private Piece nextPiece, swapPiece;

	public MoveSelector(Game game) {
		this.board = game.getBoard().toBitBoard();
		this.piece = game.getPiece();
		//this.nextPiece = game.getNextPiece();
		//this.swapPiece = game.getSwapPiece();
	}

	public Move getMove() {
		BoardEvaluator evaluator = new SapientEvaluator();

		// Enumerate all the current moves.
		List<Move> moves = findPossibleMoves(board);

		Move best = null;
		for (Move move : moves) {
			BitBoard board = this.board.clone();

			// Draw the piece on the board so we can see what it would look like after.
			Piece piece = this.piece.moveTo(move.getX(), move.getY(), move.getOrientation());

			// Draw the piece on the board so we can see what it would look like after.
			doMove(board, piece);

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

	private ArrayList<Move> findPossibleMoves(BitBoard board) {
		ArrayList<Move> moves = new ArrayList<>(board.getWidth() * 4);

		// For each possible orientation.
		for (int orientation : piece.getType().getUniqueOrientations()) {
			Piece piece = this.piece.moveTo(0, 0, orientation);

			int top = Math.max(0, board.findHighestBlock() - piece.getBoard().getHeight());

			// For each possible horizontal position.
			for (int x = -piece.getBoard().getWidth() + 1; x < board.getWidth() - 1; x++) {
				piece = piece.moveTo(x, top);
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
