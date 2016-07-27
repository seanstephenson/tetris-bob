package com.srs.tetris.bob;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.Input;
import com.srs.tetris.game.Piece;
import java.util.Objects;
import java.util.Random;

/**
 * An input supplier that executes sloppy, human-like movements.
 */
public class SloppyInputSupplier implements InputSupplier {

	private static final int INITIAL_DELAY_MAX = 1000;
	private static final int INITIAL_DELAY_MIN = 100;

	private static final int MIN_EXTRA_ROTATIONS = -1;
	private static final int MAX_EXTRA_ROTATIONS = 1;
	private static final int MIN_ROTATE_DELAY = 100;
	private static final int MAX_ROTATE_DELAY = 500;

	private static final int MIN_DROP_DELAY = 50;
	private static final int MAX_DROP_DELAY = 500;

	private static final double X_MISS_PROBABILITY = 0.25;
	private static final int MIN_X_MISS = -7;
	private static final int MAX_X_MISS = 7;
	private static final int X_MISS_CORRECTION_DELAY_MIN = 100;
	private static final int X_MISS_CORRECTION_DELAY_MAX = 500;

	private Random random = new Random();

	private Input lastInput = new Input();
	private long lastFrame = System.currentTimeMillis();

	private Move move;

	private long initialDelay;
	private long rotateDelay;
	private int rotations;
	private long dropDelay;
	private int targetX;

	@Override
	public Input createInput(Move move, Game game) {
		if (!Objects.equals(this.move, move)) {
			initMove(move, game);
		}

		long now = System.currentTimeMillis();
		long interval = now - lastFrame;
		lastFrame = now;

		Input input;
		if (move != null) {
			// If we have a move already, find the input to move the current piece to the correct place.
			input = createInputForMove(move, game, interval);

		} else {
			// We are still deciding what to do, so just return an empty input.
			input = new Input();
		}

		lastInput = input;
		return input;
	}

	private void initMove(Move move, Game game) {
		this.move = move;

		if (move != null) {
			// Select a random amount of time to "think" before moving the piece.
			initialDelay = random(INITIAL_DELAY_MIN, INITIAL_DELAY_MAX);

			if (move.getPiece() != null) {
				// Figure out how far to rotate the piece.
				rotations = game.getPiece().getOrientation() - move.getPiece().getOrientation();
				// Randomly rotate it around too far or the wrong direction.
				rotations += random(MIN_EXTRA_ROTATIONS, MAX_EXTRA_ROTATIONS) * 4;

				rotateDelay = 0;

				// Select a random delay to wait after positioning the piece correctly before dropping it.
				dropDelay = random(MIN_DROP_DELAY, MAX_DROP_DELAY);

				// Figure out where to move the piece to horizontally.
				targetX = move.getPiece().getX();

				if (random.nextDouble() < X_MISS_PROBABILITY) {
					// With a certain probability, we should "accidentally" miss the target X by some amount.
					int adjust = random(MIN_X_MISS, MAX_X_MISS);

					Piece piece = move.getPiece();
					piece = piece.moveTo(piece.getX() + adjust, game.getPiece().getY());

					// If we got adjusted inside a wall, bump it back.
					while (!game.getBoard().canPlace(piece) && adjust != 0) {
						int sign = (adjust > 0) ? -1 : 1;
						adjust += sign;
						piece = piece.moveTo(piece.getX() + sign, piece.getY());
					}

					if (game.getBoard().canPlace(piece)) {
						targetX = piece.getX();
					}
				}
			}
		}
	}

	private Input createInputForMove(Move move, Game game, long interval) {
		Input input = new Input();
		Piece piece = game.getPiece();

		if (initialDelay > 0) {
			// Waiting for the initial delay to be up.
			initialDelay -= interval;

		} else if (move.isSwap()) {
			// Swap out the current piece.
			input.setSwap(true);

		} else {
			// Provide normal input.
			if (rotations != 0) {
				rotateDelay -= interval;
				if (rotateDelay <= 0) {
					rotateDelay = random(MIN_ROTATE_DELAY, MAX_ROTATE_DELAY);

					if (rotations < 0) {
						rotations++;
						input.setRotateLeft(true);
					} else {
						rotations--;
						input.setRotateRight(true);
					}
				}
			}

			if (targetX < piece.getX()) {
				// Move left
				input.setLeft(true);

			} else if (targetX > piece.getX()) {
				// Move right
				input.setRight(true);
			}

			if (targetX == piece.getX() && targetX != move.getPiece().getX()) {
				// We were purposely going to the wrong place, so now go to the right place.
				targetX = move.getPiece().getX();

				// Wait a moment before correcting.
				initialDelay = random(X_MISS_CORRECTION_DELAY_MIN, X_MISS_CORRECTION_DELAY_MAX);
			}

			if (piece.getX() == targetX && targetX == move.getPiece().getX() && rotations == 0) {
				// The piece is in the right place.
				dropDelay -= interval;
				if (dropDelay <= 0) {
					input.setDown(true);
				}
			}
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

	private int random(int min, int max) {
		return random.nextInt((max + 1) - min) + min;
	}
}
