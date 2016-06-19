package com.srs.tetris.game;

public class Input {
	private boolean rotateLeft, rotateRight;

	private boolean left, right;
	private boolean down;

	private boolean drop;
	private boolean swap;

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isRotateLeft() {
		return rotateLeft;
	}

	public boolean isRotateRight() {
		return rotateRight;
	}

	public boolean isDrop() {
		return drop;
	}

	public boolean isSwap() {
		return swap;
	}
}
