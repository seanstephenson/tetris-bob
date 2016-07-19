package com.srs.tetris.bob;

import com.google.common.util.concurrent.MoreExecutors;
import com.srs.tetris.bob.evaluator.CountingPositionEvaluator;
import com.srs.tetris.game.DirectInput;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.game.Input;
import com.srs.tetris.game.Piece;
import com.srs.tetris.player.DirectPlayer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A computer controlled player.
 */
public class BobPlayer implements DirectPlayer, GameListener {
	private ExecutorService moveSelectionExecutor;

	private BobSettings settings;

	private Game game;
	private CountingPositionEvaluator positionEvaluator;
	private MoveSelector moveSelector;

	private Future<Move> moveFuture;
	private Input lastInput;

	public BobPlayer(BobSettings settings) {
		this.settings = settings;
	}

	@Override
	public void init(Game game) {
		this.game = game;

		if (game.getSettings().getInputMode() == GameSettings.InputMode.Direct) {
			// This game is in direct input mode, so make sure that the moves are selected in the current thread.
			moveSelectionExecutor = MoreExecutors.newDirectExecutorService();

		} else {
			// This game is in standard input mode, so use a separate thread to select moves.
			this.moveSelectionExecutor = Executors.newCachedThreadPool();
		}

		// Assume empty last input.
		lastInput = new Input();

		// Create the move selector.
		MoveEnumerator moveEnumerator = new MoveEnumerator();
		moveEnumerator.setAllowSwap(settings.isAllowSwap());

		positionEvaluator = new CountingPositionEvaluator(settings.getPositionEvaluator());
		moveSelector = new MoveSelector(moveEnumerator, positionEvaluator);

		game.addListener(this);
	}

	@Override
	public synchronized void onPieceStart(Piece piece) {
		// If there is a move still being selected from the previous position, cancel it now.
		if (moveFuture != null && !moveFuture.isDone()) {
			moveFuture.cancel(true);
			moveFuture = null;
		}

		// Select a move for the new piece.
		moveFuture = moveSelectionExecutor.submit(() -> moveSelector.getMove(new Position(game), settings.getMaxDepth()));
	}

	private Move getCurrentMove() {
		if (moveFuture != null && moveFuture.isDone()) {
			try {
				return moveFuture.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new IllegalStateException(e);
			}

		} else {
			return null;
		}
	}

	@Override
	public DirectInput directInput() {
		// Get the current move that we are trying to execute.
		Move move = getCurrentMove();

		if (move != null) {
			// If we have a move already, pass it back.
			if (!move.isSwap()) {
				// The move is for the current piece, so pass back its coordinates.
				return new DirectInput(move.getPiece().getX(), move.getPiece().getY(), move.getPiece().getOrientation());

			} else {
				// The move is a swap.
				return DirectInput.swap();
			}

		} else {
			// Otherwise we aren't ready yet, so make no move yet.
			return null;
		}
	}

	@Override
	public synchronized Input input() {
		// Get the current move that we are trying to execute.
		Move move = getCurrentMove();

		Input input;
		if (move != null) {
			// If we have a move already, find the input to move the current piece to the correct place.
			input = createInputForMove(move);

		} else {
			// We are still deciding what to do, so just return an empty input.
			input = new Input();
		}

		lastInput = input;
		return input;
	}

	private Input createInputForMove(Move move) {
		Input input = new Input();

		if (move.isSwap()) {
			// Swap out the current piece.
			input.setSwap(true);

		} else if (move.getPiece().getOrientation() != game.getPiece().getOrientation()) {
			if (shouldRotateRight(move)) {
				input.setRotateRight(true);
			} else {
				input.setRotateLeft(true);
			}

		} else if (move.getPiece().getX() < game.getPiece().getX()) {
			// Move left
			input.setLeft(true);

		} else if (move.getPiece().getX() > game.getPiece().getX()) {
			// Move right
			input.setRight(true);

		} else {
			// It's in the right place, so drop it.
			input.setDrop(true);
		}

		// Make sure we release buttons if necessary before pressing them again.
		handleButtonReleases(input);

		return input;
	}

	private void handleButtonReleases(Input input) {
		// Some buttons require a frame of "not pressed" before they will be registered again.  To make sure we don't get stuck
		// in a "pressed" state that doesn't actually do anything, we will unset the buttons here if they were pressed last frame.
		if (lastInput.isRotateLeft()) input.setRotateLeft(false);
		if (lastInput.isRotateRight()) input.setRotateRight(false);
		if (lastInput.isDrop()) input.setDrop(false);
		if (lastInput.isSwap()) input.setSwap(false);
		if (lastInput.isLeft()) input.setLeft(false);
		if (lastInput.isRight()) input.setRight(false);
	}

	private boolean shouldRotateRight(Move move) {
		// Rotate right if it is only one step away.  Otherwise rotate left.
		return Math.floorMod(game.getPiece().getOrientation() - 1, 4) == move.getPiece().getOrientation();
	}

	public long getPositionsEvaluated() {
		return positionEvaluator.getPositionsEvaluated();
	}
}
