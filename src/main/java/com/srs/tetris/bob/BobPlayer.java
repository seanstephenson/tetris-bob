package com.srs.tetris.bob;

import com.google.common.util.concurrent.MoreExecutors;
import com.srs.tetris.bob.evaluator.BoardEvaluator;
import com.srs.tetris.bob.evaluator.SapientEvaluator;
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

	private Game game;
	private BoardEvaluator boardEvaluator;

	private Future<Move> moveFuture;
	private Input lastInput;

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

		// Make sure we have a board evaluator.
		if (boardEvaluator == null) {
			boardEvaluator = new SapientEvaluator();
		}

		game.addListener(this);
	}

	@Override
	public synchronized void onPieceStart() {
		// If there is a move still being selected from the previous position, cancel it now.
		if (moveFuture != null && !moveFuture.isDone()) {
			moveFuture.cancel(true);
			moveFuture = null;
		}

		// Select a move for the new piece.
		moveFuture = moveSelectionExecutor.submit(() -> new MoveSelector(game).getMove());
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
	public Piece directInput() {
		// Get the current move that we are trying to execute.
		Move move = getCurrentMove();

		if (move != null) {
			// If ew have a move already, pass back the coordinates directly.
			return game.getPiece().moveTo(move.getX(), move.getY(), move.getOrientation());

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

		if (move.getOrientation() != game.getPiece().getOrientation()) {
			if (shouldRotateRight(move)) {
				input.setRotateRight(true);
			} else {
				input.setRotateLeft(true);
			}

		} else if (move.getX() < game.getPiece().getX()) {
			// Move left
			input.setLeft(true);

		} else if (move.getX() > game.getPiece().getX()) {
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
		return Math.floorMod(game.getPiece().getOrientation() - 1, 4) == move.getOrientation();
	}

	public void setBoardEvaluator(BoardEvaluator boardEvaluator) {
		this.boardEvaluator = boardEvaluator;
	}
}
