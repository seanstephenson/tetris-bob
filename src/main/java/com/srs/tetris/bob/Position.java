package com.srs.tetris.bob;

import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Board;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.Piece;

/**
 * Encapsulates all attributes that make up a game position.
 */
public class Position {
	private BitBoard board;
	private Piece piece;
	private Piece nextPiece;
	private Piece swapPiece;
	private boolean isPieceSwapped;

	public Position(Game game) {
		this(
			game.getBoard().toBitBoard(),
			game.getPiece(),
			game.getNextPiece(),
			game.getSwapPiece(),
			game.isPieceSwapped()
		);
	}

	public Position(BitBoard board) {
		this(board, null);
	}

	public Position(BitBoard board, Piece piece) {
		this(board, piece, null, null, false);
	}

	public Position(BitBoard board, Piece piece, Piece nextPiece, Piece swapPiece, boolean isPieceSwapped) {
		this.board = board;
		this.piece = piece;
		this.nextPiece = nextPiece;
		this.swapPiece = swapPiece;
		this.isPieceSwapped = isPieceSwapped;
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
	public Piece getPiece() {
		return piece;
	}

	/**
	 * The next piece to drop.
	 */
	public Piece getNextPiece() {
		return nextPiece;
	}

	/**
	 * The piece that is currently swapped out.
	 */
	public Piece getSwapPiece() {
		return swapPiece;
	}

	/**
	 * Indicates if the last piece was swapped out.
	 */
	public boolean isPieceSwapped() {
		return isPieceSwapped;
	}
}
