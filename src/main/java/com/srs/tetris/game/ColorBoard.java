package com.srs.tetris.game;

/**
 * A grid of squares that can each have an individual color.
 */
public class ColorBoard extends Board {
	private int[] colors;

	public ColorBoard(int width, int height) {
		super(width, height);
		colors = new int[width * height];
	}

	public ColorBoard(int[][] grid) {
		this(grid[0].length, grid.length);

		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				int value = grid[y][x];
				super.set(x, y, value != 0);
				setColor(x, y, Color.values()[value]);
			}
		}
	}

	@Override
	public int[][] getGrid() {
		int[][] output = new int[getHeight()][getWidth()];
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				output[y][x] = colors[y * getWidth() + x];
			}
		}
		return output;
	}

	public Color getColor(int x, int y) {
		return Color.values()[colors[y * getWidth() + x]];
	}

	private void setColor(int x, int y, Color color) {
		colors[y * getWidth() + x] = color.ordinal();
	}

	@Override
	public void place(Piece piece) {
		super.place(piece.getBoard(), piece.getX(), piece.getY());

		Board pieceBoard = piece.getBoard();

		for (int pieceX = 0; pieceX < pieceBoard.getWidth(); pieceX++) {
			for (int pieceY = 0; pieceY < pieceBoard.getHeight(); pieceY++) {
				int placeX = piece.getX() + pieceX;
				int placeY = piece.getY() + pieceY;
				if (checkRange(placeX, placeY) && !pieceBoard.isEmpty(pieceX, pieceY)) {
					setColor(placeX, placeY, piece.getColor());
				}
			}
		}
	}

	private boolean checkRange(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}

	@Override
	public void removeLine(int y) {
		super.removeLine(y);

		while (y > 0) {
			// Copy the line above down to this one.
			for (int x = 0; x < getWidth(); x++) {
				setColor(x, y, getColor(x, y - 1));
			}

			// Move to the line above.
			y--;
		}

		// Empty the top line.
		for (int x = 0; x < getWidth(); x++) {
			setColor(x, 0, Color.Empty);
		}
	}

	public Board toBitBoard() {
		return new Board(getGrid());
	}

	@Override
	public ColorBoard clone() {
		ColorBoard clone = (ColorBoard) super.clone();
		clone.colors = colors.clone();
		return clone;
	}

	@Override
	public void place(Board piece, int x, int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void set(int x, int y, boolean value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Board rotateLeft() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Board rotateRight() {
		throw new UnsupportedOperationException();
	}
}
