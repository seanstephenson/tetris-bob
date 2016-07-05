package com.srs.tetris.game;

public abstract class AbstractBoard<T> implements Board<T> {
	private int width;
	private int height;

	protected AbstractBoard(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	protected AbstractBoard clone() {
		try {
			return (AbstractBoard) super.clone();

		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException();
		}
	}
}
