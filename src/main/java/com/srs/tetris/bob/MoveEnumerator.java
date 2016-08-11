package com.srs.tetris.bob;

import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Piece;
import com.srs.tetris.game.PieceType;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates all possible moves for a position.
 */
public class MoveEnumerator {

	/**
	 * Indicates if piece swapping is allowed.
	 */
	private boolean allowSwap = true;

	/**
	 * Find all possible moves that can be played from the given position.
	 */
	public List<Move> findPossibleMoves(Position position) {
		BitBoard board = position.getBoard();
		PieceType piece = position.getPiece();

		List<Move> moves = new ArrayList<>(board.getWidth() * 4);
		findPossibleMoves(board, piece, moves);

		if (allowSwap && position.canSwap()) {
			PieceType swapPiece = position.getSwapPiece();
			if (swapPiece == null) {
				swapPiece = position.getNextPiece();
			}

			// If we have a swap piece or a next piece, and it isn't the same as the current piece, then generate a swap move as well.
			if (piece != swapPiece) {
				moves.add(Move.swap());
			}
		}

		return moves;
	}

	private void findPossibleMoves(BitBoard board, PieceType pieceType, List<Move> moves) {
		// For each possible orientation.
		for (int orientation : pieceType.getUniqueOrientations()) {
			Piece piece = new Piece(pieceType, orientation, 0, 0);

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
	}

	public void setAllowSwap(boolean allowSwap) {
		this.allowSwap = allowSwap;
	}
}
