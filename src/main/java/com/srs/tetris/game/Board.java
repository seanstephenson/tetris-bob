package com.srs.tetris.game;

public interface Board<T> extends Cloneable {
	/**
	 * Returns the width of this board.
	 */
	public int getWidth();

	/**
	 * Returns the height of this board.
	 */
	public int getHeight();

	/**
	 * Indicates if the given square is empty.  The position must be in bounds, or else the behavior is undefined.
	 */
	boolean isEmpty(int x, int y);

	/**
	 * Removes the value at the given position, leaving it empty.  The position must be in bounds, or else the behavior is undefined.
	 */
	void remove(int x, int y);

	/**
	 * Gets the value at the given position.  The position must be in bounds, or else the behavior is undefined.
	 */
	T get(int x, int y);

	/**
	 * Sets the value at the given position.  The position must be in bounds, or else the behavior is undefined.
	 */
	void set(int x, int y, T value);

	/**
	 * Places the given piece on the board, overwriting any previous values at that position.  If the piece is
	 * partially out of bounds, the values that are out of bounds will be ignored.
	 */
	void place(Piece piece);

	/**
	 * Indicates if the given piece can be placed without overwriting any non-empty value or colliding with walls.
	 */
	default boolean canPlace(Piece piece) {
		Board pieceBoard = piece.getBoard();

		for (int pieceX = 0; pieceX < pieceBoard.getWidth(); pieceX++) {
			for (int pieceY = 0; pieceY < pieceBoard.getHeight(); pieceY++) {
				int placeX = piece.getX() + pieceX;
				int placeY = piece.getY() + pieceY;

				if (!pieceBoard.isEmpty(pieceX, pieceY) && (!checkRange(placeX, placeY) || !isEmpty(placeX, placeY))) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Removes the given line, moving all other lines that were above it down.
	 */
	default void removeLine(int y) {
		assertRange(0, y);

		while (y > 0) {
			// Copy the line above down to this one.
			for (int x = 0; x < getWidth(); x++) {
				set(x, y, get(x, y - 1));
			}

			// Move to the line above.
			y--;
		}

		// Empty the top line.
		for (int x = 0; x < getWidth(); x++) {
			remove(x, 0);
		}
	}

	/**
	 * Indicates if the given column is entirely empty.
	 */
	default boolean isColumnEmpty(int x) {
		assertRange(x, 0);

		for (int y = 0; y < getHeight(); y++) {
			if (!isEmpty(x, y)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Indicates if the given line is entirely empty.
	 */
	default boolean isLineEmpty(int y) {
		assertRange(0, y);

		for (int x = 0; x < getWidth(); x++) {
			if (!isEmpty(x, y)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Indicates if the given line is entirely complete.
	 */
	default boolean isLineComplete(int y) {
		assertRange(0, y);

		for (int x = 0; x < getWidth(); x++) {
			if (isEmpty(x, y)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks the range of the arguments, throwing an assertion error if assertions are enabled.
	 */
	default void assertRange(int x, int y) {
		assert x >= 0 && x < getWidth() : String.format("x=%d, width=%d", x, getWidth());
		assert y >= 0 && y < getHeight() : String.format("y=%d, height=%d", y, getHeight());
	}

	/**
	 * Checks the range of the arguments, returning true if they are in bounds and false otherwise.
	 */
	default boolean checkRange(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
}
