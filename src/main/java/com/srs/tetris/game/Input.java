package com.srs.tetris.game;

public class Input implements Cloneable {
	private boolean rotateLeft, rotateRight;

	private boolean left, right;
	private boolean down;

	private boolean drop;
	private boolean swap;

	@Override
	public Input clone() {
		try {
			return (Input) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	public boolean isRotateLeft() {
		return rotateLeft;
	}

	public void setRotateLeft(boolean rotateLeft) {
		this.rotateLeft = rotateLeft;
	}

	public boolean isRotateRight() {
		return rotateRight;
	}

	public void setRotateRight(boolean rotateRight) {
		this.rotateRight = rotateRight;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isDrop() {
		return drop;
	}

	public void setDrop(boolean drop) {
		this.drop = drop;
	}

	public boolean isSwap() {
		return swap;
	}

	public void setSwap(boolean swap) {
		this.swap = swap;
	}
}
