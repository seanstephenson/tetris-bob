package com.srs.tetris.game;

public class Board implements Cloneable {

	private static final int MAX_WIDTH = 32;

	private int width;
	private int height;
	private int[] grid;

	private int lineMask;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new int[height];

		if (this.width > MAX_WIDTH) {
			throw new IllegalArgumentException(String.format("width=%d, width must be 32 or less"));
		}

		this.lineMask = (int) ((1L << width) - 1);
	}

	public Board(int[][] grid) {
		this(grid[0].length, grid.length);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				set(x, y, grid[y][x] != 0);
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
				output[y][x] = get(x, y) ? 1 : 0;
			}
		}
		return output;
	}

	public boolean isEmpty(int x, int y) {
		return !get(x, y);
	}

	public Color getColor(int x, int y) {
		return get(x, y) ? Color.Green : Color.Empty;
	}

	public boolean get(int x, int y) {
		assert x >= 0 && x < width : String.format("x=%d, width=%d", x, width);
		assert y >= 0 && y < height : String.format("y=%d, height=%d", y, height);
		return (grid[y] & mask(x)) > 0;
	}

	public void set(int x, int y, boolean value) {
		assert x >= 0 && x < width : String.format("x=%d, width=%d", x, width);
		assert y >= 0 && y < height : String.format("y=%d, height=%d", y, height);
		if (value) {
			grid[y] |= mask(x);
		} else {
			grid[y] &= ~mask(x);
		}
	}

	private int mask(int x) {
		return 1 << x;
	}

	public Board rotateLeft() {
		Board rotated = new Board(height, width);

		for (int x = 0; x < rotated.width; x++) {
			for (int y = 0; y < rotated.height; y++) {
				boolean value = get((rotated.height - 1) - y, x);
				rotated.set(x, y, value);
			}
		}

		return rotated;
	}

	public Board rotateRight() {
		return rotateLeft().rotateLeft().rotateLeft();
	}

	public void place(Piece piece) {
		place(piece.getBoard(), piece.getX(), piece.getY(), true);
	}

	public void remove(Piece piece) {
		place(piece.getBoard(), piece.getX(), piece.getY(), false);
	}

	public void place(Board piece, int x, int y, boolean value) {
		for (int pieceY = 0; pieceY < piece.height; pieceY++) {
			int pieceLine = piece.grid[pieceY];
			if (pieceLine != 0) {
				int placeY = y + pieceY;
				if (placeY >= 0 && placeY < height) {
					int mask = (x > 0 ? pieceLine << x : pieceLine >> -x) & lineMask;
					if (value) {
						grid[placeY] |= mask;
					} else {
						grid[placeY] &= ~mask;
					}
				}
			}
		}
	}

	public boolean canPlace(Piece piece) {
		return canPlace(piece.getBoard(), piece.getX(), piece.getY());
	}

	public boolean canPlace(Board piece, int x, int y) {
		for (int pieceY = 0; pieceY < piece.height; pieceY++) {
			int pieceLine = piece.grid[pieceY];
			if (pieceLine != 0) {
				int placeY = y + pieceY;
				if (placeY >= 0 && placeY < height) {
					int placeX = x;

					while (placeX < 0) {
						// This is out of bounds for X, so if the piece has any filled spaces for those bits, it won't fit.
						if ((pieceLine & 1) != 0) {
							return false;
						}

						pieceLine >>= 1;
						placeX++;
					}

					// Shift the piece into X position.
					int mask = pieceLine << placeX;

					// If it has any bits set that are outside of the line then it won't fit.
					if (mask > lineMask) {
						return false;
					}

					// Finally check if it collides with any existing pieces.
					if ((mask & grid[placeY]) != 0) {
						return false;
					}

				} else {
					// This is out of bounds for Y, so if the piece has any filled spaces at this line, it won't fit.
					if (piece.grid[pieceY] != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean isColumnEmpty(int x) {
		for (int y = 0; y < getHeight(); y++) {
			if (!isEmpty(x, y)) {
				return false;
			}
		}
		return true;
	}

	public boolean isLineEmpty(int y) {
		return grid[y] == 0;
	}

	public boolean isLineComplete(int y) {
		return grid[y] == lineMask;
	}

	public void removeLine(int y) {
		while (y > 0) {
			// Copy the line above down to this one.
			grid[y] = grid[y - 1];

			// Move to the line above.
			y--;
		}

		// Empty the top line.
		grid[0] = 0;
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
