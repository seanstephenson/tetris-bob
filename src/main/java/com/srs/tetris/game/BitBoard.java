package com.srs.tetris.game;

/**
 * A grid of squares, each of which can be filled or empty.
 */
public class BitBoard extends AbstractBoard<Boolean> {

	// The maximum width is limited to th
	private static final int MAX_WIDTH = 32;

	// A bit board, with one element per row.
	private int[] grid;

	// The mask for a full line.
	private int lineMask;

	public BitBoard(int width, int height) {
		super(width, height);
		this.grid = new int[height];

		if (this.getWidth() > MAX_WIDTH) {
			throw new IllegalArgumentException(String.format("width=%d, must be %d or less", getWidth(), MAX_WIDTH));
		}

		this.lineMask = (int) ((1L << width) - 1);
	}

	public BitBoard(int[][] grid) {
		this(grid[0].length, grid.length);

		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				set(x, y, grid[y][x] != 0);
			}
		}
	}

	public int[][] getGrid() {
		int[][] output = new int[getHeight()][getWidth()];
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				output[y][x] = get(x, y) ? 1 : 0;
			}
		}
		return output;
	}

	@Override
	public boolean isEmpty(int x, int y) {
		return !get(x, y);
	}

	@Override
	public void remove(int x, int y) {
		set(x, y, false);
	}

	@Override
	public Boolean get(int x, int y) {
		assertRange(x, y);
		return (grid[y] & mask(x)) > 0;
	}

	@Override
	public void set(int x, int y, Boolean value) {
		assertRange(x, y);
		if (value) {
			grid[y] |= mask(x);
		} else {
			grid[y] &= ~mask(x);
		}
	}

	private int mask(int x) {
		return 1 << x;
	}

	@Override
	public void place(Piece piece) {
		place(piece.getBoard(), piece.getX(), piece.getY());
	}

	public void place(BitBoard piece, int x, int y) {
		for (int pieceY = 0; pieceY < piece.getHeight(); pieceY++) {
			int pieceLine = piece.grid[pieceY];
			if (pieceLine != 0) {
				int placeY = y + pieceY;
				if (placeY >= 0 && placeY < getHeight()) {
					int mask = (x > 0 ? pieceLine << x : pieceLine >> -x) & lineMask;
					grid[placeY] |= mask;
				}
			}
		}
	}

	@Override
	public boolean canPlace(Piece piece) {
		return canPlace(piece.getBoard(), piece.getX(), piece.getY());
	}

	public boolean canPlace(BitBoard piece, int x, int y) {
		for (int pieceY = 0; pieceY < piece.getHeight(); pieceY++) {
			int pieceLine = piece.grid[pieceY];
			if (pieceLine != 0) {
				int placeY = y + pieceY;
				if (placeY >= 0 && placeY < getHeight()) {
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

	@Override
	public boolean isLineEmpty(int y) {
		// More efficient implementation, just check the entire row at once.
		return grid[y] == 0;
	}

	@Override
	public boolean isLineComplete(int y) {
		// More efficient implementation, check if all bits are set at once.
		return grid[y] == lineMask;
	}

	@Override
	public void removeLine(int y) {
		// More efficient implementation that moves row by row.
		while (y > 0) {
			// Copy the line above down to this one.
			grid[y] = grid[y - 1];

			// Move to the line above.
			y--;
		}

		// Empty the top line.
		grid[0] = 0;
	}

	/**
	 * Returns the number of filled, non-empty blocks in the given line.
	 */
	public int countBlocksInLine(int y) {
		assertRange(0, y);
		return Integer.bitCount(grid[y]);
	}

	/**
	 * Returns the line number of the highest non-empty line.
	 */
	public int findHighestBlock() {
		for (int y = 0; y < getHeight(); y++) {
			if (grid[y] != 0) {
				return y;
			}
		}
		return getHeight();
	}

	public BitBoard rotateLeft() {
		BitBoard rotated = new BitBoard(getHeight(), getWidth());

		for (int x = 0; x < rotated.getWidth(); x++) {
			for (int y = 0; y < rotated.getHeight(); y++) {
				boolean value = get((rotated.getHeight() - 1) - y, x);
				rotated.set(x, y, value);
			}
		}

		return rotated;
	}

	public BitBoard rotateRight() {
		return rotateLeft().rotateLeft().rotateLeft();
	}

	@Override
	public BitBoard clone() {
		BitBoard clone = (BitBoard) super.clone();
		clone.grid = grid.clone();
		return clone;
	}
}
