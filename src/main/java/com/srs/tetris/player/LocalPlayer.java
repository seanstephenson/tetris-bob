package com.srs.tetris.player;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.Input;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * A player that uses local key events in order to handle this.
 */
public class LocalPlayer implements EventHandler<KeyEvent>, Player {
	private Input input;

	public LocalPlayer() {
		input = new Input();
	}

	@Override
	public void handle(KeyEvent event) {
		boolean pressed = event.getEventType() == KeyEvent.KEY_PRESSED;

		switch (event.getCode()) {
			case Q:
				input.setRotateLeft(pressed);
				break;

			case E:
			case W:
			case UP:
				input.setRotateRight(pressed);
				break;

			case A:
			case LEFT:
				input.setLeft(pressed);
				break;

			case D:
			case RIGHT:
				input.setRight(pressed);
				break;

			case S:
			case DOWN:
				input.setDown(pressed);
				break;

			case SPACE:
				input.setDrop(pressed);
				break;

			case TAB:
				input.setSwap(pressed);
				break;
		}
	}

	@Override
	public Input input() {
		return input.clone();
	}
}
