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
	 * Find all possible moves that can be played from the given position.
	 */
	public List<Move> findPossibleMoves(Position position) {
		BitBoard board = position.getBoard();
		Piece piece = position.getPiece();

		ArrayList<Move> moves = new ArrayList<>(board.getWidth() * 4);

		// For each possible orientation.
		for (int orientation : piece.getType().getUniqueOrientations()) {
			piece = piece.moveTo(0, 0, orientation);

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
