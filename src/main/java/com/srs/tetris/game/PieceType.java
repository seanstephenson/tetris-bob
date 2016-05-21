package com.srs.tetris.game;

import java.util.Random;

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

	private static Random random = new Random();

	private Board[] boards;

	private PieceType(Board board) {
		createBoards(board);
	}

	private void createBoards(Board board) {
		boards = new Board[] {
			board,
			board.rotateLeft(),
			board.rotateLeft().rotateLeft(),
			board.rotateLeft().rotateLeft().rotateLeft()
		};
	}

	public Board getBoard() {
		return getBoard(0);
	}

	public Board getBoard(int orientation) {
		return boards[orientation % 4];
	}

	public static PieceType random() {
		return values()[random.nextInt(values().length)];
	}
}
