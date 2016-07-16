package com.srs.tetris.game;

/**
 * The state for a direct input player.
 */
public class DirectInput {
	private int x, y;
	private int orientation;
	private boolean swap;

	public DirectInput(int x, int y, int orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
	}

	private DirectInput(boolean swap) {
		this.swap = swap;
	}

	public static DirectInput swap() {
		return new DirectInput(true);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getOrientation() {
		return orientation;
	}

	public boolean isSwap() {
		return swap;
	}
}
