package com.srs.tetris.game;

public class Input {
	private boolean left, right;
	private boolean rotate;
	private boolean drop;

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isRotate() {
		return rotate;
	}

	public boolean isDrop() {
		return drop;
	}
}
