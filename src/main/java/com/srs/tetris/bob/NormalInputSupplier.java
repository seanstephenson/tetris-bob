package com.srs.tetris.bob;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.Input;
import com.srs.tetris.game.Piece;

public class NormalInputSupplier implements InputSupplier {
	private Input lastInput;
	private boolean rapidMovement;

	public NormalInputSupplier() {
		// Assume empty last input.
		lastInput = new Input();
	}

	@Override
	public Input createInput(Move move, Game game) {
		Input input;
		if (move != null) {
			// If we have a move already, find the input to move the current piece to the correct place.
			input = createInputForMove(move, game);

		} else {
			// We are still deciding what to do, so just return an empty input.
			input = new Input();
		}

		lastInput = input;
		return input;
	}

	private Input createInputForMove(Move move, Game game) {
		Input input = new Input();

		if (move.isSwap()) {
			// Swap out the current piece.
			input.setSwap(true);

		} else if (move.getPiece().getOrientation() != game.getPiece().getOrientation()) {
			if (shouldRotateRight(move, game.getPiece())) {
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
			if (isRapidMovement()) {
				input.setDrop(true);
			} else {
				input.setDown(true);
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

		if (isRapidMovement()) {
			if (lastInput.isLeft()) input.setLeft(false);
			if (lastInput.isRight()) input.setRight(false);
		}
	}

	private boolean shouldRotateRight(Move move, Piece piece) {
		// Rotate right if it is only one step away.  Otherwise rotate left.
		return Math.floorMod(piece.getOrientation() - 1, 4) == move.getPiece().getOrientation();
	}

	/**
	 * Indicates if "rapid movement" is enabled, meaning the left and right buttons will be pressed each frame rather than simply
	 * held in.
	 */
	public boolean isRapidMovement() {
		return rapidMovement;
	}

	public void setRapidMovement(boolean rapidMovement) {
		this.rapidMovement = rapidMovement;
	}
}
