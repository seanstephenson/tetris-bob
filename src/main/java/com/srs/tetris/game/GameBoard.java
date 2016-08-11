package com.srs.tetris.game;

import java.util.Arrays;

/**
 * A grid of squares that can each have an individual color.
 */
public class GameBoard extends AbstractBoard<Color> {
	private static final Color[] COLORS = Color.values();

	private int[] grid;

	public GameBoard(int width, int height) {
		super(width, height);
		grid = new int[width * height];
	}

	public static GameBoard from(String text) {
		return from(text.split("\n"));
	}

	public static GameBoard from(String[] lines) {
		char[][] values = tokenize(lines);
		GameBoard board = new GameBoard(values[0].length, values.length);
		board.fillFromChars(values);
		return board;
	}

	public GameBoard(BitBoard other, Color color) {
		this(other.getWidth(), other.getHeight());

		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (!other.isEmpty(x, y)) {
					set(x, y, color);
				}
			}
		}
	}

	public BitBoard toBitBoard() {
		return new BitBoard(this);
	}

	@Override
	public boolean isEmpty(int x, int y) {
		return get(x, y) == Color.Empty;
	}

	@Override
	public void remove(int x, int y) {
		set(x, y, Color.Empty);
	}

	@Override
	public Color get(int x, int y) {
		assertRange(x, y);
		return color(grid[y * getWidth() + x]);
	}

	@Override
	public void set(int x, int y, Color color) {
		assertRange(x, y);
		assert color != null : "Color cannot be null";
		grid[y * getWidth() + x] = color.ordinal();
	}

	private Color color(int value) {
		return COLORS[value];
	}

	@Override
	public void place(Piece piece) {
		BitBoard pieceBoard = piece.getBoard();

		for (int pieceX = 0; pieceX < pieceBoard.getWidth(); pieceX++) {
			for (int pieceY = 0; pieceY < pieceBoard.getHeight(); pieceY++) {
				int placeX = piece.getX() + pieceX;
				int placeY = piece.getY() + pieceY;
				if (checkRange(placeX, placeY) && !pieceBoard.isEmpty(pieceX, pieceY)) {
					set(placeX, placeY, piece.getColor());
				}
			}
		}
	}

	@Override
	protected char valueToChar(Color color) {
		PieceType pieceType = PieceType.forColor(color);
		return pieceType != null ? pieceType.name().charAt(0) : '.';
	}

	@Override
	protected Color charToValue(char c) {
		if (c == '.') return Color.Empty;
		return PieceType.valueOf(String.valueOf(c)).getColor();
	}

	@Override
	public GameBoard clone() {
		GameBoard clone = (GameBoard) super.clone();
		clone.grid = grid.clone();
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GameBoard)) return false;
		GameBoard other = (GameBoard) obj;

		if (getWidth() != other.getWidth()) return false;
		if (getHeight() != other.getHeight()) return false;

		return Arrays.equals(grid, other.grid);
	}
}
