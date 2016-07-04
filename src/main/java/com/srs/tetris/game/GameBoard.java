package com.srs.tetris.game;

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

	public GameBoard(int[][] grid) {
		this(grid[0].length, grid.length);

		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				set(x, y, color(grid[y][x]));
			}
		}
	}

	public int[][] getGrid() {
		int[][] output = new int[getHeight()][getWidth()];
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				output[y][x] = get(x, y).ordinal();
			}
		}
		return output;
	}

	public BitBoard toBitBoard() {
		return new BitBoard(getGrid());
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
	public GameBoard clone() {
		GameBoard clone = (GameBoard) super.clone();
		clone.grid = grid.clone();
		return clone;
	}
}
