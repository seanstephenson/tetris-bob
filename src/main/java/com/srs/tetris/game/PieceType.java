package com.srs.tetris.game;

import java.util.Random;

public enum PieceType {
	Bar(new Board(new int[][]{
		{0, 0, 0, 0},
		{1, 1, 1, 1},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
	}), Color.Blue),

	Square(new Board(new int[][]{
		{1, 1},
		{1, 1},
	}), Color.Green),

	J(new Board(new int[][]{
		{1, 0, 0},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Purple),

	L(new Board(new int[][]{
		{0, 0, 1},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Gray),

	S(new Board(new int[][]{
		{0, 1, 1},
		{1, 1, 0},
		{0, 0, 0},
	}), Color.Orange),

	Z(new Board(new int[][]{
		{1, 1, 0},
		{0, 1, 1},
		{0, 0, 0},
	}), Color.Cyan),

	T(new Board(new int[][]{
		{0, 0, 0},
		{1, 1, 1},
		{0, 1, 0},
	}), Color.Red);

	private static Random random = new Random();

	private Board[] boards;
	private Color color;

	private PieceType(Board board, Color color) {
		createBoards(board);
		this.color = color;
	}

	private void createBoards(Board board) {
		boards = new Board[] {
			board,
			board.rotateLeft(),
			board.rotateLeft().rotateLeft(),
			board.rotateLeft().rotateLeft().rotateLeft()
		};
	}

	public Color getColor() {
		return color;
	}

	public Board getBoard() {
		return getBoard(0);
	}

	public Board getBoard(int orientation) {
		return boards[Math.floorMod(orientation, 4)];
	}

	public static PieceType random() {
		return values()[random.nextInt(values().length)];
	}
}
