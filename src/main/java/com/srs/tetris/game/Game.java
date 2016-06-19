package com.srs.tetris.game;

import com.srs.tetris.player.Player;
import java.util.concurrent.TimeUnit;

public class Game {

	private static final int DEFAULT_BOARD_WIDTH = 10;
	private static final int DEFAULT_BOARD_HEIGHT = 20;

	private static final long DEFAULT_STARTING_DROP_INTERVAL = 1000;
	private static final long DEFAULT_PIECE_MOVE_INTERVAL = 250;
	private static final long DEFAULT_FRAME_INTERVAL = 10;

	private Player player;
	private Board board;

	private Piece piece;

	private Piece nextPiece;
	private Piece swapPiece;

	private Input lastInput;
	private Input input;

	private int totalPieces;
	private int score;
	private int lines;

	private long lastFrame;
	private long frameInterval;

	private long dropInterval;
	private long dropDelay;

	private long pieceMoveInterval;
	private long pieceMoveDelay;

	private boolean pieceSwapped;

	private enum Status { New, InProgress, Complete }
	private Status status = Status.New;

	public Game(Player player) {
		this.player = player;
	}

	public void run() {
		if (status != Status.New) {
			throw new IllegalStateException("Cannot run a game that is not new.");
		}

		status = Status.InProgress;

		// Set up the game
		setupGame();

		while (!isGameOver()) {
			long frame = System.currentTimeMillis();
			long interval = frame - lastFrame;

			// Get the input state from the player
			lastInput = input;
			input = player.input(this);

			// Update the game state
			updateGame(interval);

			// Sleep until the next frame
			lastFrame = frame;
			sleep(frameInterval);
		}

		status = Status.Complete;
	}

	private void setupGame() {
		// Create the empty game board.
		board = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);

		// Set up the intervals.
		frameInterval = DEFAULT_FRAME_INTERVAL;
		dropInterval = DEFAULT_STARTING_DROP_INTERVAL;
		pieceMoveInterval = DEFAULT_PIECE_MOVE_INTERVAL;

		// Create a random next piece, and drop it.
		nextPiece = Piece.random();
		dropNextPiece();

		// Set the time for the last frame.
		lastFrame = System.nanoTime();
	}

	private void updateGame(long interval) {
		// Check if the current piece needs to move down.
		updatePieceDrop(interval);

		// See if the piece needs to move left or right.
		updatePieceMoveLeftRight(interval);

		// See if the piece needs to rotate.
		updatePieceRotate();

		// See if the piece needs to be swapped.
		updatePieceSwap();
	}

	private void updatePieceDrop(long interval) {
		dropDelay -= interval;
		if (dropDelay <= 0) {
			// Enough time has passed that the piece should move.
			if (!movePieceDown()) {
				// The piece is already at the bottom or is blocked.  So place it.
				placePiece();
			}

			// Reset the delay for the next drop.
			dropDelay = dropInterval;
		}
	}

	private void updatePieceMoveLeftRight(long interval) {
		if (!input.isLeft() && !input.isRight()) {
			// If no buttons are pressed reset the piece move delay to allow the piece to move again immediately.
			pieceMoveDelay = 0;

		} else {
			// Otherwise, if they are holding left or right only move the piece after the piece delay expires.
			pieceMoveDelay -= interval;

			if (pieceMoveDelay <= 0) {
				if (input.isLeft() && board.canPlace(piece.moveLeft())) {
					piece = piece.moveLeft();
				}
				if (input.isRight() && board.canPlace(piece.moveRight())) {
					piece = piece.moveRight();
				}

				// Reset the delay for the next move.
				pieceMoveDelay = pieceMoveInterval;
			}
		}
	}

	private void updatePieceRotate() {
		if ((lastInput == null || !lastInput.isRotateLeft()) && input.isRotateLeft() && board.canPlace(piece.rotateLeft())) {
			piece = piece.rotateLeft();
		}
		if ((lastInput == null || !lastInput.isRotateRight()) && input.isRotateRight() && board.canPlace(piece.rotateRight())) {
			piece = piece.rotateRight();
		}
	}

	private void updatePieceSwap() {
		if (input.isSwap() && !pieceSwapped) {
			// Swap out the current piece
			Piece current = piece;
			piece = swapPiece;
			swapPiece = current;

			if (piece == null) {
				// If there was no piece already in the swap position, just drop the next piece.
				dropNextPiece();

			} else {
				// Otherwise, move the piece up to the top of the board.
				movePieceToTopCenter();
			}

			// Don't allow it to be swapped again until they place this one.
			pieceSwapped = true;
		}
	}

	private void dropNextPiece() {
		// Place the next piece at the top of the game board.
		piece = nextPiece;
		movePieceToTopCenter();

		// Create the next next piece.
		nextPiece = Piece.random();

		// Reset the drop delay
		dropDelay = dropInterval;

		// Keep track of the total pieces played.
		totalPieces++;
	}

	private void placePiece() {
		// Place the piece on the board.
		board.place(piece);

		// Check for complete lines.
		checkCompleteLines();

		// Drop the next piece.
		dropNextPiece();

		// Allow the piece to be swapped out again.
		pieceSwapped = false;
	}

	private void movePieceToTopCenter() {
		piece = piece.moveTo((board.getWidth() - piece.getBoard().getWidth()) / 2, 0);
	}

	private boolean movePieceDown() {
		if (board.canPlace(piece.moveDown())) {
			// Move the piece down.
			piece = piece.moveDown();
			return true;

		} else {
			// The piece couldn't move.
			return false;
		}
	}

	private void checkCompleteLines() {
		// Loop over each line and check if it is complete.
	}

	private boolean isGameOver() {
		return false;
	}

	private void sleep(long interval) {
		try {
			TimeUnit.MILLISECONDS.sleep(interval);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public Board getBoard() {
		return board;
	}

	public Piece getPiece() {
		return piece;
	}

	public Piece getNextPiece() {
		return nextPiece;
	}

	public Piece getSwapPiece() {
		return swapPiece;
	}
}
