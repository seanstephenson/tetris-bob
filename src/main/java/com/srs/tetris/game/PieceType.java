package com.srs.tetris.game;

public enum PieceType {
	Bar(new Board(new int[][]{
		{0, 0, 0, 0},
		{1, 1, 1, 1},
		{0, 0, 0, 0},
		{0, 0, 0, 0},
	})),

	Square(new Board(new int[][]{
		{1, 1},
		{1, 1},
	})),

	J(new Board(new int[][]{
		{1, 0, 0},
		{1, 1, 1},
		{0, 0, 0},
	})),

	L(new Board(new int[][]{
		{0, 0, 1},
		{1, 1, 1},
		{0, 0, 0},
	})),

	S(new Board(new int[][]{
		{0, 1, 1},
		{1, 1, 0},
		{0, 0, 0},
	})),

	Z(new Board(new int[][]{
		{1, 1, 0},
		{0, 1, 1},
		{0, 0, 0},
	})),

	T(new Board(new int[][]{
		{0, 0, 0},
		{1, 1, 1},
		{0, 1, 0},
	}));

	private Board board;

	private PieceType(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}
}
