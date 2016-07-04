package com.srs.tetris.game;

import java.util.Random;

public enum PieceType {
	I(new BitBoard(new int[][]{
		{0, 0, 0, 0},
		{1, 1, 1, 1},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
	}), Color.Cyan),

	O(new BitBoard(new int[][]{
		{1, 1},
		{1, 1},
	}), Color.Yellow),

	T(new BitBoard(new int[][]{
		{0, 1, 0},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Purple),

	S(new BitBoard(new int[][]{
		{0, 1, 1},
		{1, 1, 0},
		{0, 0, 0},
	}), Color.Green),

	Z(new BitBoard(new int[][]{
		{1, 1, 0},
		{0, 1, 1},
		{0, 0, 0},
	}), Color.Red),

	J(new BitBoard(new int[][]{
		{1, 0, 0},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Blue),

	L(new BitBoard(new int[][]{
		{0, 0, 1},
		{1, 1, 1},
		{0, 0, 0},
	}), Color.Orange);

	private static Random random = new Random();

	private BitBoard[] boards;
	private Color color;

	private PieceType(BitBoard board, Color color) {
		createBoards(board);
		this.color = color;
	}

	private void createBoards(BitBoard board) {
		boards = new BitBoard[] {
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

	public BitBoard getBoard(int orientation) {
		return boards[Math.floorMod(orientation, 4)];
	}

	public static PieceType random() {
		return values()[random.nextInt(values().length)];
	}
}
