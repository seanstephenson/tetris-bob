package com.srs.tetris.game;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

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
	private int[] uniqueOrientations;

	private PieceType(BitBoard board, Color color) {
		createBoards(board);
		this.color = color;
		uniqueOrientations = computeUniqueOrientations();
	}

	private void createBoards(BitBoard board) {
		boards = new BitBoard[] {
			board,
			board.rotateLeft(),
			board.rotateLeft().rotateLeft(),
			board.rotateLeft().rotateLeft().rotateLeft()
		};
	}

	private int[] computeUniqueOrientations() {
		Map<Integer, BitBoard> uniqueOrientations = new TreeMap<>();

		for (int orientation = 0; orientation < 4; orientation++) {
			BitBoard cropped = getBoard(orientation).crop();
			if (!uniqueOrientations.values().contains(cropped)) {
				uniqueOrientations.put(orientation, cropped);
			}
		}

		return uniqueOrientations.keySet().stream().mapToInt(i -> i).toArray();
	}

	public BitBoard getBoard() {
		return getBoard(0);
	}

	public BitBoard getBoard(int orientation) {
		assert orientation >= 0 && orientation < 4 : String.format("Illegal orientation: %d", orientation);
		return boards[orientation];
	}

	public Color getColor() {
		return color;
	}

	public int[] getUniqueOrientations() {
		return uniqueOrientations;
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
