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
	private InputSupplier inputSupplier;

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

		// Create the move selector.
		MoveEnumerator moveEnumerator = new MoveEnumerator();
		moveEnumerator.setAllowSwap(settings.isAllowSwap());

		positionEvaluator = new CountingPositionEvaluator(settings.getPositionEvaluator());
		moveSelector = new MoveSelector(moveEnumerator, positionEvaluator);

		inputSupplier = settings.getInputSupplier();

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
		// Get the input for the current move that we are trying to execute.
		return inputSupplier.createInput(getCurrentMove(), game);
	}

	public long getPositionsEvaluated() {
		return positionEvaluator.getPositionsEvaluated();
	}
}
