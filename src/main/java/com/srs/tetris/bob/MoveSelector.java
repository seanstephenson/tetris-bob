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
	private boolean allowSwap = true;

	private Piece nextPiece;
	private Piece swapPiece;

	public MoveSelector(Game game) {
		this.board = game.getBoard().toBitBoard();
		this.piece = game.getPiece();
		this.nextPiece = game.getNextPiece();
		this.swapPiece = game.getSwapPiece();
	}

	public Move getMove() {
		BoardEvaluator evaluator = new SapientEvaluator();

		// Enumerate all the current moves.
		List<Move> moves = new MoveEnumerator(
			board,
			piece,
			allowSwap ? swapPiece : null
		).findPossibleMoves();

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

	public boolean isAllowSwap() {
		return allowSwap;
	}

	public void setAllowSwap(boolean allowSwap) {
		this.allowSwap = allowSwap;
	}
}
