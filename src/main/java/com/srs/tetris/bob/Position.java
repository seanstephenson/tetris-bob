package com.srs.tetris.bob;

import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Board;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.Piece;
import com.srs.tetris.game.PieceType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Encapsulates all attributes that make up a game position.
 */
public class Position {
	private BitBoard board;
	private PieceType piece;
	private List<PieceType> nextPieces;
	private PieceType swapPiece;
	private boolean isPieceSwapped;

	public Position(Game game) {
		this(
			game.getBoard().toBitBoard(),
			game.getPiece() != null ? game.getPiece().getType() : null,
			game.getNextPieces().stream().map(Piece::getType).collect(toList()),
			game.getSwapPiece() != null ? game.getSwapPiece().getType() : null,
			game.isPieceSwapped()
		);
	}

	public Position(BitBoard board) {
		this(board, null);
	}

	public Position(BitBoard board, PieceType piece) {
		this(board, piece, Collections.emptyList(), null, false);
	}

	public Position(BitBoard board, PieceType piece, PieceType nextPiece, PieceType swapPiece, boolean isPieceSwapped) {
		this(board, piece, nextPiece != null ? Arrays.asList(nextPiece) : Collections.emptyList(), swapPiece, isPieceSwapped);
	}

	public Position(BitBoard board, PieceType piece, List<PieceType> nextPieces, PieceType swapPiece, boolean isPieceSwapped) {
		this.board = board;
		this.piece = piece;
		this.nextPieces = nextPieces;
		this.swapPiece = swapPiece;
		this.isPieceSwapped = isPieceSwapped;
	}

	/**
	 * Does the given move and then returns the resulting position.
	 */
	public Position doMove(Move move) {
		// First, place the piece on the board.
		BitBoard board = place(move.getPiece());

		//
		if (getPiece() == move.getPiece().getType()) {
			// This was a normal move (not a swap).
			PieceType piece = getNextPiece();
			List<PieceType> nextPieces = removeFirst(getNextPieces());

			return new Position(board, piece, nextPieces, getSwapPiece(), isPieceSwapped());

		} else {
			// This was a swap move.
			List<PieceType> nextPieces = getNextPieces();
			PieceType swapPiece = getPiece();

			PieceType swapped = getSwapPiece();
			if (swapped == null) {
				swapped = getNextPiece();
				nextPieces = removeFirst(nextPieces);
			}

			assert move.getPiece().getType() == swapped : "invalid piece type for move: " + move.getPiece().getType();

			PieceType piece = first(nextPieces);
			nextPieces = removeFirst(nextPieces);

			return new Position(board, piece, nextPieces, swapPiece, true);
		}
	}

	private <T> T first(List<T> list) {
		return list.isEmpty() ? null : list.get(0);
	}

	private <T> List<T> removeFirst(List<T> list) {
		return list.isEmpty() ? list : list.subList(1, list.size());
	}

	private BitBoard place(Piece piece) {
		BitBoard board = getBoard().clone();
		board.place(piece);

		// Now, remove completed lines, checking only the lines that were impacted by the piece.
		int top = Math.max(0, piece.getY());
		int bottom = Math.min(board.getHeight(), top + piece.getBoard().getHeight());

		for (int y = top; y < bottom; y++) {
			if (board.isLineComplete(y)) {
				board.removeLine(y);
			}
		}

		return board;
	}

	/**
	 * The game board.
	 */
	public BitBoard getBoard() {
		return board;
	}

	/**
	 * The current piece that is in play.
	 */
	public PieceType getPiece() {
		return piece;
	}

	/**
	 * The next piece to drop.
	 */
	public PieceType getNextPiece() {
		return !nextPieces.isEmpty() ? nextPieces.get(0) : null;
	}

	/**
	 * The next pieces that will drop.
	 */
	public List<PieceType> getNextPieces() {
		return nextPieces;
	}

	/**
	 * The piece that is currently swapped out.
	 */
	public PieceType getSwapPiece() {
		return swapPiece;
	}

	/**
	 * Indicates if the last piece was swapped out.
	 */
	public boolean isPieceSwapped() {
		return isPieceSwapped;
	}
}
