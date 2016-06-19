package com.srs.tetris.game;

import java.util.Random;

public class Piece implements Cloneable {
	private static Random random = new Random();

	private PieceType type;
	private Color color;
	private int x, y;
	private int orientation;

	public Piece(PieceType type, Color color) {
		this(type, color, 0);
	}

	public Piece(PieceType type, Color color, int orientation) {
		this(type, color, orientation, 0, 0);
	}

	public Piece(PieceType type, Color color, int orientation, int x, int y) {
		this.type = type;
		this.color = color;
		this.x = x;
		this.y = y;
		this.orientation = orientation;
	}

	public PieceType getType() {
		return type;
	}

	public Board getBoard() {
		return getType().getBoard(getOrientation());
	}

	public Color getColor() {
		return color;
	}

	public Piece moveTo(int x, int y) {
		return moveTo(x, y, orientation);
	}

	public Piece moveTo(int x, int y, int orientation) {
		Piece moved = this.clone();
		moved.x = x;
		moved.y = y;
		moved.orientation = orientation;
		return moved;
	}

	public Piece moveDown() {
		return moveTo(x, y + 1, orientation);
	}

	public Piece moveLeft() {
		return moveTo(x - 1, y, orientation);
	}

	public Piece moveRight() {
		return moveTo(x + 1, y, orientation);
	}

	public Piece rotateLeft() {
		return moveTo(x, y, orientation + 1);
	}

	public Piece rotateRight() {
		return moveTo(x, y, orientation - 1);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Piece clone() {
		try {
			return (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException();
		}
	}

	public int getOrientation() {
		return orientation;
	}

	public static Piece random() {
		return new Piece(
			PieceType.random(),
			Color.random(),
			random.nextInt(4)
		);
	}
}
