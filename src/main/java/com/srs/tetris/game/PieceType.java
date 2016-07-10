package com.srs.tetris.game;

import java.util.Random;

public enum PieceType {
	I(BitBoard.from(new String[]{
		". . . .",
		"X X X X",
		". . . .",
		". . . .",
	}), Color.Cyan),

	O(BitBoard.from(new String[]{
		"X X",
		"X X",
	}), Color.Yellow),

	T(BitBoard.from(new String[]{
		". X .",
		"X X X",
		". . .",
	}), Color.Purple),

	S(BitBoard.from(new String[]{
		". X X",
		"X X .",
		". . .",
	}), Color.Green),

	Z(BitBoard.from(new String[]{
		"X X .",
		". X X",
		". . .",
	}), Color.Red),

	J(BitBoard.from(new String[]{
		"X . .",
		"X X X",
		". . .",
	}), Color.Blue),

	L(BitBoard.from(new String[]{
		". . X",
		"X X X",
		". . .",
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
		assert orientation >= 0 && orientation < 4 : String.format("illegal orientation: %d", orientation);
		return boards[orientation];
	}

	public static PieceType random() {
		return values()[random.nextInt(values().length)];
	}

	public static PieceType forColor(Color color) {
		for (PieceType pieceType : values()) {
			if (pieceType.getColor() == color) {
				return pieceType;
			}
		}
		return null;
	}
}
