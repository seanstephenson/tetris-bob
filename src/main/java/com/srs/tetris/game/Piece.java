package com.srs.tetris.game;

public class Piece {
	private PieceType type;
	private Color color;
	private int x, y;
	private int orientation;

	public Piece(PieceType type, Color color) {
		this(type, color, 0, 0, 0);
	}

	public Piece(PieceType type, Color color, int x, int y, int orientation) {
		this.type = type;
		this.color = color;
		this.x = x;
		this.y = y;
		this.orientation = orientation;
	}

	public PieceType getType() {
		return type;
	}

	public Color getColor() {
		return color;
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
}
