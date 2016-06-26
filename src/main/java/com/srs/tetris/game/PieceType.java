package com.srs.tetris.game;

import java.util.Random;

public enum PieceType {
	I(new Board(new int[][]{
		{0, 0, 0, 0},
		{1, 1, 1, 1},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
	}), Color.Cyan),

	O(new Board(new int[][]{
		{1, 1},
		{1, 1},
	}), Color.Yellow),

	T(new Board(new int[][]{
		{0, 1, 0},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Purple),

	S(new Board(new int[][]{
		{0, 1, 1},
		{1, 1, 0},
		{0, 0, 0},
	}), Color.Green),

	Z(new Board(new int[][]{
		{1, 1, 0},
		{0, 1, 1},
		{0, 0, 0},
	}), Color.Red),

	J(new Board(new int[][]{
		{1, 0, 0},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Blue),

	L(new Board(new int[][]{
		{0, 0, 1},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Orange);

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
