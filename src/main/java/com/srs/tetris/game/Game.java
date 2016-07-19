package com.srs.tetris.game;

import com.srs.tetris.player.DirectPlayer;
import com.srs.tetris.player.Player;
import com.srs.tetris.replay.Replay;
import com.srs.tetris.replay.ReplayGenerator;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Game {

	private GameSettings settings;
	private PieceGenerator pieceGenerator;
	private Executor listenerExecutor;

	private Player player;
	private GameBoard board;

	private Piece piece;

	private Queue<Piece> nextPieces;
	private Piece swapPiece;

	private Input lastInput;
	private Input input;

	private long totalPieces;
	private long completedLines;
	private long level;
	private long score;

	private long lastFrame;

	private long dropInterval;
	private long dropDelay;
	private long pieceMoveDelay;
	private long pieceManualDownDelay;

	private boolean pieceSwapped;

	private List<GameListener> listeners = new ArrayList<>();

	public enum Status { New, InProgress, Complete, Cancelled, Error }
	private volatile Status status = Status.New;
	private Throwable error;

	private Instant startTime;
	private Instant endTime;

	private ReplayGenerator replayGenerator;

	public Game(GameSettings settings) {
		this.settings = settings;
		this.player = settings.getPlayer();
		this.pieceGenerator = settings.getPieceGenerator();
		this.listenerExecutor = settings.getListenerExecutor();
	}

	public void init() {
		// Validate the game settings.
		validate();

		// Initialize the player.
		player.init(this);

		// Set up the game.
		setupGame();
	}

	public void run() {
		if (status != Status.New) {
			throw new IllegalStateException("Cannot run a game that is not new.");
		}

		status = Status.InProgress;
		startTime = Instant.now();

		// Start the replay if necessary.
		startReplay();

		try {
			notifyListeners((listener) -> listener.onGameStart());

			// Drop the first piece.
			dropNextPiece();

			while (!isGameOver()) {
				long frame = System.currentTimeMillis();
				long interval = frame - lastFrame;
				lastFrame = frame;

				// Get the input state from the player.
				updateInput();

				// Update the game state.
				updateGame(interval);

				notifyListeners((listener) -> listener.onFrame());

				// Sleep until the next frame.
				sleep(settings.getFrameInterval());
			}

		} catch (Throwable throwable) {
			status = Status.Error;
			error = throwable;
			throw throwable;

		} finally {
			endTime = Instant.now();
			notifyListeners((listener) -> listener.onGameOver());
		}
	}

	private void startReplay() {
		// Create the replay generator.
		if (settings.isGenerateReplay()) {
			// Add the replay generator in first position so it always receives events first.
			// Otherwise other listeners that try to do something with the replay on game over
			// won't be dealing with the finished product.
			listeners.add(0, replayGenerator = new ReplayGenerator(this));
		}
	}

	private void validate() {
		if (settings.getInputMode() == GameSettings.InputMode.Direct && !(player instanceof DirectPlayer)) {
			throw new IllegalStateException("Invalid player for direct input mode");
		}
	}

	private void setupGame() {
		// Create the empty game board.
		board = new GameBoard(settings.getWidth(), settings.getHeight());

		// Create a random next piece, and drop it.
		nextPieces = new ArrayDeque<>();
		fillNextPieces();

		// Set the time for the last frame.
		lastFrame = System.currentTimeMillis();

		// Assume empty input at the beginning of the game.
		lastInput = new Input();
		input = new Input();

		// Calculate the starting level.
		updateLevel();
	}

	private void updateInput() {
		// Get input from the player.

		if (player instanceof DirectPlayer && settings.getInputMode() == GameSettings.InputMode.Direct) {
			// This is a direct player and we are in direct input mode, so get it directly.
			DirectInput move = ((DirectPlayer) player).directInput();

			lastInput = new Input();
			input = new Input();

			if (move != null) {
				if (!move.isSwap()) {
					// The move is for the current piece.  Move it to the correct location and drop it.
					piece = piece.moveTo(move.getX(), move.getY(), move.getOrientation());
					input.setDrop(true);

					assert board.canPlace(piece) : "Invalid move, could not place on board";

				} else {
					// The move is a swap.
					input.setSwap(true);
				}
			}

		} else {
			// Otherwise, it's just a normal input player.
			lastInput = input;
			input = player.input();
		}
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
			swapPiece = current.moveTo(0, 0, 0);

			if (piece == null) {
				// If there was no piece already in the swap position, just drop the next piece.
				dropNextPiece();

			} else {
				// Otherwise, move the piece up to the top of the board.
				movePieceToTopCenter();

				Piece started = this.piece;
				notifyListeners((listener) -> listener.onPieceStart(started));
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
					pieceMoveDelay = settings.getPieceMoveInterval();
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
				pieceManualDownDelay = settings.getPieceManualDownInterval();
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
		piece = nextPieces.remove();
		movePieceToTopCenter();

		// If the new piece is blocked, the game is over.
		if (!board.canPlace(piece)) {
			status = Status.Complete;
		}

		// Create the next next piece.
		fillNextPieces();

		// Reset the drop delay
		dropDelay = dropInterval;

		// Keep track of the total pieces played.
		totalPieces++;

		// Notify listeners that the piece is starting.
		Piece started = piece;
		notifyListeners((listener) -> listener.onPieceStart(started));
	}

	private void fillNextPieces() {
		// Fill up the next pieces queue.
		while (nextPieces.size() < settings.getNextPieceCount()) {
			nextPieces.add(pieceGenerator.generate());
		}
	}

	private void placePiece() {
		// Place the piece on the board and remove completed lines.
		board.place(piece);

		// Notify listeners that the piece just landed.
		Piece landed = piece;
		notifyListeners((listener) -> listener.onPieceLand(landed));

		// Update the score.
		score += computeScoreDeltaForPiece();

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
		// Remove the completed lines from the board.
		int lines = board.removeCompleteLines();

		if (lines > 0) {
			// If lines were complete, update the score.
			completedLines += lines;
			score += computeScoreDeltaForLines(lines);

			// Update the current level.
			updateLevel();

			if (settings.getMaxLines() > 0 && completedLines > settings.getMaxLines()) {
				status = Status.Complete;
			}

			// If there is supposed to be a line complete delay wait now (typically to allow for animation).
			sleep(settings.getLineCompleteDelay());
		}
	}

	private void updateLevel() {
		// Update the level based on the number of lines completed.
		level = completedLines / settings.getLinesPerLevel();

		// Update the drop interval (faster with every level).
		dropInterval = (long) (settings.getStartingDropInterval() / ((level * settings.getLevelAccelerator()) + 1));
		if (dropInterval < settings.getMinDropInterval()) {
			dropInterval = settings.getMinDropInterval();
		}
	}

	private long computeScoreDeltaForPiece() {
		// This is the original NES scoring system.
		return (level + 1) * 5;
	}

	private long computeScoreDeltaForLines(int lines) {
		assert lines >= 1 && lines <= 4 : "Unexpected number of completed lines: " + lines;

		// This is the original NES scoring system.
		int[] multipliers = {40, 100, 300, 1200};
		return multipliers[lines - 1] * (level + 1);
	}

	public boolean isGameOver() {
		return status != Status.New && status != Status.InProgress;
	}

	private void sleep(long interval) {
		try {
			if (interval > 0) {
				TimeUnit.MILLISECONDS.sleep(interval);
			}

			if (Thread.interrupted()) {
				cancel();
			}

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void cancel() {
		status = Status.Cancelled;
	}

	private void notifyListeners(Consumer<? super GameListener> consumer) {
		listenerExecutor.execute(() -> listeners.forEach(consumer));
	}

	public void addListener(GameListener listener) {
		listeners.add(listener);
	}

	public GameSettings getSettings() {
		return settings;
	}

	public Player getPlayer() {
		return player;
	}

	public GameBoard getBoard() {
		return board;
	}

	public Piece getPiece() {
		return piece;
	}

	public Piece getNextPiece() {
		return nextPieces.peek();
	}

	public Collection<Piece> getNextPieces() {
		return nextPieces;
	}

	public Piece getSwapPiece() {
		return swapPiece;
	}

	public boolean isPieceSwapped() {
		return pieceSwapped;
	}

	public long getScore() {
		return score;
	}

	public long getLevel() {
		return level;
	}

	public long getCompletedLines() {
		return completedLines;
	}

	public long getTotalPieces() {
		return totalPieces;
	}

	public Status getStatus() {
		return status;
	}

	public Throwable getError() {
		return error;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public Replay getReplay() {
		return replayGenerator != null ? replayGenerator.getReplay() : null;
	}
}
