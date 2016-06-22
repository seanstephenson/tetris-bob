package com.srs.tetris.bob;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.game.Input;
import com.srs.tetris.player.Player;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A computer controlled player.
 */
public class BobPlayer implements Player, GameListener {
	private ExecutorService executor;
	private Game game;

	private Future<Move> moveFuture;
	private Input lastInput;

	@Override
	public void init(Game game) {
		this.game = game;
		this.executor = Executors.newCachedThreadPool();

		lastInput = new Input();

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
		moveFuture = executor.submit(() -> new MoveSelector(game).getMove());
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
	}

	private boolean shouldRotateRight(Move move) {
		// Rotate right if it is only one step away.  Otherwise rotate left.
		return Math.floorMod(game.getPiece().getOrientation() - 1, 4) == move.getOrientation();
	}
}
