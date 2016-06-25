package com.srs.tetris.game;

import com.srs.tetris.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Game {

	private static final int DEFAULT_BOARD_WIDTH = 10;
	private static final int DEFAULT_BOARD_HEIGHT = 20;

	private static final long DEFAULT_STARTING_DROP_INTERVAL = 1000;
	private static final long DEFAULT_MIN_DROP_INTERVAL = 100;
	private static final long DEFAULT_PIECE_MOVE_INTERVAL = 150;
	private static final long DEFAULT_PIECE_MANUAL_DOWN_INTERVAL = 100;
	private static final long DEFAULT_FRAME_INTERVAL = 25;

	private static final int DEFAULT_LINES_PER_LEVEL = 10;
	private static final double DEFAULT_LEVEL_ACCELERATOR = 0.25;

	private static final Executor DEFAULT_LISTENER_EXECUTOR = Executors.newCachedThreadPool();

	private Executor listenerExecutor;

	private PieceGenerator pieceGenerator;

	private Player player;
	private Board board;

	private Piece piece;

	private Piece nextPiece;
	private Piece swapPiece;

	private Input lastInput;
	private Input input;

	private int totalPieces;
	private int completedLines;
	private int level;
	private int score;

	private int linesPerLevel;
	private double levelAccelerator;

	private long lastFrame;
	private long frameInterval;

	private long startingDropInterval;
	private long minDropInterval;
	private long dropInterval;
	private long dropDelay;

	private long pieceMoveInterval;
	private long pieceMoveDelay;

	private long pieceManualDownInterval;
	private long pieceManualDownDelay;

	private boolean pieceSwapped;

	private List<GameListener> listeners = new ArrayList<>();

	private enum Status { New, InProgress, Complete }
	private Status status = Status.New;

	public Game(Player player) {
		this.player = player;
	}

	public void init() {
		// Initialize the player.
		player.init(this);

		// Create a piece generator if there isn't one set.
		if (pieceGenerator == null) {
			pieceGenerator = new BagPieceGenerator();
		}

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
			lastFrame = frame;

			// Get the input state from the player.
			lastInput = input;
			input = player.input();

			// Update the game state.
			updateGame(interval);

			notifyListeners((listener) -> listener.onFrame());

			// Sleep until the next frame.
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

		// Set up the game parameters.
		frameInterval = DEFAULT_FRAME_INTERVAL;
		startingDropInterval = DEFAULT_STARTING_DROP_INTERVAL;
		minDropInterval = DEFAULT_MIN_DROP_INTERVAL;
		dropInterval = DEFAULT_STARTING_DROP_INTERVAL;
		pieceMoveInterval = DEFAULT_PIECE_MOVE_INTERVAL;
		pieceManualDownInterval = DEFAULT_PIECE_MANUAL_DOWN_INTERVAL;

		linesPerLevel = DEFAULT_LINES_PER_LEVEL;
		levelAccelerator = DEFAULT_LEVEL_ACCELERATOR;

		// Create a random next piece, and drop it.
		nextPiece = pieceGenerator.generate();
		dropNextPiece();

		// Set the time for the last frame.
		lastFrame = System.currentTimeMillis();

		// Assume empty input at the beginning of the game.
		lastInput = new Input();
		input = new Input();

		// Calculate the starting level.
		updateLevel();
	}

	private void updateGame(long interval) {
		// See if the piece needs to be swapped.
		updatePieceSwap();

		// See if the piece needs to move left or right.
		updatePieceMoveLeftRight(interval);

		// See if the piece needs to rotate.
		updatePieceRotate();

		// Check if the current piece needs to move down.
		updatePieceDrop(interval);
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

	private void updatePieceMoveLeftRight(long interval) {
		if (!input.isLeft() && !input.isRight()) {
			// If no buttons are pressed reset the piece move delay to allow the piece to move again immediately.
			pieceMoveDelay = 0;

		} else {
			// Otherwise, if they are holding an arrow only move the piece after the piece delay expires.
			pieceMoveDelay -= interval;

			if (pieceMoveDelay <= 0) {
				boolean moved = false;
				if (input.isLeft() && board.canPlace(piece.moveLeft())) {
					piece = piece.moveLeft();
					moved = true;
				}
				if (input.isRight() && board.canPlace(piece.moveRight())) {
					piece = piece.moveRight();
					moved = true;
				}

				// Reset the delay for the next move.
				if (moved) {
					pieceMoveDelay = pieceMoveInterval;
				}
			}
		}
	}

	private void updatePieceRotate() {
		if (!lastInput.isRotateLeft() && input.isRotateLeft()) {
			Piece rotated = adjustPieceAfterRotation(piece.rotateLeft());
			if (rotated != null) {
				piece = rotated;
			}
		}
		if (!lastInput.isRotateRight() && input.isRotateRight()) {
			Piece rotated = adjustPieceAfterRotation(piece.rotateRight());
			if (rotated != null) {
				piece = rotated;
			}
		}
	}

	private Piece adjustPieceAfterRotation(Piece piece) {
		Piece original = piece;
		if (!board.canPlace(piece)) {
			piece = original.moveLeft();
			if (!board.canPlace(piece)) {
				piece = original.moveRight();
				if (!board.canPlace(piece)) {
					piece = original.moveLeft().moveLeft();
					if (!board.canPlace(piece)) {
						piece = original.moveRight().moveRight();
						if (!board.canPlace(piece)) {
							piece = original.moveDown();
							if (!board.canPlace(piece)) {
								piece = original.moveDown().moveDown();
								if (!board.canPlace(piece)) {
									piece = original.moveUp();
									if (!board.canPlace(piece)) {
										return null;
									}
								}
							}
						}
					}
				}
			}
		}
		return piece;
	}

	private void updatePieceDrop(long interval) {
		boolean moveDown = false;

		// Check for a manual move down.
		if (!input.isDown()) {
			// If they aren't holding down, reset the delay so the piece can be manually moved down immediately.
			pieceManualDownDelay = 0;

		} else {
			// If they are holding down, only allow the piece to go down if the delay expires.
			pieceManualDownDelay -= interval;

			if (pieceManualDownDelay <= 0) {
				// The delay expired, so move it down.
				moveDown = true;

				// Reset the manual delay until it moves down again.
				pieceManualDownDelay = pieceManualDownInterval;
			}
		}

		// Check if the automatic drop delay is expired.
		dropDelay -= interval;
		if (dropDelay <= 0) {
			// The delay epired, so move it down.
			moveDown = true;
		}

		if (moveDown) {
			// They are pressing down or enough time has passed that the piece should move.
			if (!movePieceDown()) {
				// The piece is already at the bottom or is blocked.  So place it.
				placePiece();
			}

			// Reset the delay for the next drop.
			dropDelay = dropInterval;
		}

		if (!lastInput.isDrop() && input.isDrop()) {
			// They pressed the drop button, so move it all the way down and then place it.
			while (movePieceDown());
			placePiece();
		}
	}

	private void dropNextPiece() {
		// Place the next piece at the top of the game board.
		piece = nextPiece;
		movePieceToTopCenter();

		// If the new piece is blocked, the game is over.
		if (!board.canPlace(piece)) {
			status = Status.Complete;
		}

		// Create the next next piece.
		nextPiece = pieceGenerator.generate();

		// Reset the drop delay
		dropDelay = dropInterval;

		// Keep track of the total pieces played.
		totalPieces++;

		// Notify listeners that the piece is starting.
		notifyListeners((listener) -> listener.onPieceStart());
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
		// Find the center of the piece, taking into account empty columns.
		double center = piece.getBoard().getWidth() / 2.0;
		int x = 0;
		while (piece.getBoard().isColumnEmpty(x++)) {
			center += 0.5;
		}

		x = piece.getBoard().getWidth() - 1;
		while (piece.getBoard().isColumnEmpty(x--)) {
			center -= 0.5;
		}

		// Find the top of the piece, taking into account empty rows.
		int top = 0;
		while (piece.getBoard().isLineEmpty(top)) {
			top++;
		}

		// Place the piece at the top center of the board, taking into account the empty space.
		piece = piece.moveTo((int) Math.round(board.getWidth() / 2.0 - center), -top);
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
		int lines = 0;
		for (int y = 0; y < board.getHeight(); y++) {
			if (board.isLineComplete(y)) {
				board.removeLine(y);
				lines++;
			}
		}

		if (lines > 0) {
			completedLines += lines;
			score += computeScoreDeltaForLines(lines);

			// Update the current level.
			updateLevel();
		}
	}

	private void updateLevel() {
		// Update the level based on the number of lines completed.
		level = completedLines / linesPerLevel;

		// Update the drop interval (faster with every level).
		dropInterval = (long) (startingDropInterval / ((level * levelAccelerator) + 1));
		if (dropInterval < minDropInterval) {
			dropInterval = minDropInterval;
		}
	}

	private int computeScoreDeltaForLines(int lines) {
		assert lines >= 1 && lines <= 4 : "Unexpected number of completed lines: " + lines;

		// This is the original NES scoring system.
		int[] multipliers = {40, 100, 300, 1200};
		return multipliers[lines - 1] * level;
	}

	private boolean isGameOver() {
		return status == Status.Complete;
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

	public PieceGenerator getPieceGenerator() {
		return pieceGenerator;
	}

	public void setPieceGenerator(PieceGenerator pieceGenerator) {
		this.pieceGenerator = pieceGenerator;
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

	public int getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public int getCompletedLines() {
		return completedLines;
	}

	public int getTotalPieces() {
		return totalPieces;
	}
}
