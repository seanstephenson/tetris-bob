package com.srs.tetris.game;

public class Board implements Cloneable {
	private int width;
	private int height;
	private int[] grid;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new int[width * height];
	}

	public Board(int[][] grid) {
		this.width = grid[0].length;
		this.height = grid.length;
		this.grid = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				set(x, y, grid[y][x]);
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[][] getGrid() {
		int[][] output = new int[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				output[y][x] = get(x, y);
			}
		}
		return output;
	}

	public boolean isEmpty(int x, int y) {
		return get(x, y) == 0;
	}

	public Color getColor(int x, int y) {
		return Color.values()[get(x, y)];
	}

	public int get(int x, int y) {
		assert x >= 0 && x < width : String.format("x=%d, width=%d", x, width);
		assert y >= 0 && y < height : String.format("y=%d, height=%d", y, height);
		return grid[y * width + x];
	}

	public void set(int x, int y, int value) {
		assert x >= 0 && x < width;
		assert y >= 0 && y < height;
		grid[y * width + x] = value;
	}

	public Board rotateLeft() {
		Board rotated = new Board(height, width);

		for (int x = 0; x < rotated.width; x++) {
			for (int y = 0; y < rotated.height; y++) {
				int value = get((rotated.height - 1) - y, x);
				rotated.set(x, y, value);
			}
		}

		return rotated;
	}

	public Board rotateRight() {
		return rotateLeft().rotateLeft().rotateLeft();
	}

	public void place(Piece piece) {
		place(piece.getBoard(), piece.getX(), piece.getY(), piece.getColor().ordinal());
	}

	public void place(Board piece, int x, int y, int value) {
		for (int pieceX = 0; pieceX < piece.width; pieceX++) {
			for (int pieceY = 0; pieceY < piece.height; pieceY++) {
				if (checkRange(x + pieceX, y + pieceY) && !piece.isEmpty(pieceX, pieceY)) {
					set(x + pieceX, y + pieceY, value);
				}
			}
		}
	}

	public boolean canPlace(Piece piece) {
		return canPlace(piece.getBoard(), piece.getX(), piece.getY());
	}

	public boolean canPlace(Board piece, int x, int y) {
		for (int pieceX = 0; pieceX < piece.width; pieceX++) {
			for (int pieceY = 0; pieceY < piece.height; pieceY++) {
				if (!piece.isEmpty(pieceX, pieceY)) {
					int placeX = x + pieceX, placeY = y + pieceY;
					if (!checkRange(placeX, placeY) || !isEmpty(placeX, placeY)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean isLineComplete(int y) {
		for (int x = 0; x < getWidth(); x++) {
			if (isEmpty(x, y)) {
				return false;
			}
		}
		return true;
	}

	public void fillLine(int y, int value) {
		for (int x = 0; x < getWidth(); x++) {
			set(x, y, value);
		}
	}

	public void removeLine(int y) {
		while (y > 0) {
			// Copy the line above down to this one.
			for (int x = 0; x < getWidth(); x++) {
				set(x, y, get(x, y - 1));
			}

			// Move to the line above.
			y--;
		}

		// Empty the top line.
		fillLine(0, 0);
	}

	private boolean checkRange(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	@Override
	public Board clone() {
		try {
			Board clone = (Board) super.clone();
			clone.grid = grid.clone();
			return clone;

		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException();
		}
	}
}
