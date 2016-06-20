package com.srs.tetris.game;

import com.srs.tetris.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class Game {

	private static final int DEFAULT_BOARD_WIDTH = 10;
	private static final int DEFAULT_BOARD_HEIGHT = 20;

	private static final long DEFAULT_STARTING_DROP_INTERVAL = 1000;
	private static final long DEFAULT_PIECE_MOVE_INTERVAL = 250;
	private static final long DEFAULT_FRAME_INTERVAL = 25;

	private static final Executor DEFAULT_LISTENER_EXECUTOR = Executors.newCachedThreadPool();

	private Executor listenerExecutor;

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

	private List<GameListener> listeners = new ArrayList<>();

	private enum Status { New, InProgress, Complete }
	private Status status = Status.New;

	public Game(Player player) {
		this.player = player;
	}

	public void init() {
		// Set up the game.
		setupGame();
	}

	public void run() {
		if (status != Status.New) {
			throw new IllegalStateException("Cannot run a game that is not new.");
		}

		status = Status.InProgress;

		notifyListeners((listener) -> listener.onGameStart());

		while (!isGameOver()) {
			long frame = System.currentTimeMillis();
			long interval = frame - lastFrame;

			// Get the input state from the player.
			lastInput = input;
			input = player.input(this);

			// Update the game state.
			updateGame(interval);

			notifyListeners((listener) -> listener.onFrame());

			// Sleep until the next frame.
			lastFrame = frame;
			sleep(frameInterval);
		}

		status = Status.Complete;

		notifyListeners((listener) -> listener.onGameOver());
	}

	private void setupGame() {
		// By default, execute each listener in a different thread.  This way, no matter how long they take to do their logic
		// (e.g. painting the game or getting network input), the game will run at the predetermined speed.
		listenerExecutor = DEFAULT_LISTENER_EXECUTOR;

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
		lastFrame = System.currentTimeMillis();
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
		if (dropDelay <= 0 || input.isDown()) {
			// They are pressing down or enough time has passed that the piece should move.
			if (!movePieceDown()) {
				// The piece is already at the bottom or is blocked.  So place it.
				placePiece();
			}

			// Reset the delay for the next drop.
			dropDelay = dropInterval;
		}

		if ((lastInput == null || !lastInput.isDrop()) && input.isDrop()) {
			// They pressed the drop button, so move it all the way down and then place it.
			while (movePieceDown());
			placePiece();
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

	private void notifyListeners(Consumer<? super GameListener> consumer) {
		listenerExecutor.execute(() -> listeners.forEach(consumer));
	}

	public void addListener(GameListener listener) {
		listeners.add(listener);
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
