package com.srs.tetris.game

import org.junit.Test

class PieceTest {
	@Test
	void board() {
		def piece = new Piece(PieceType.L)
		assert piece.board.grid == PieceType.L.getBoard(0).grid

		piece = piece.rotateLeft()
		assert piece.board.grid == PieceType.L.getBoard(1).grid
	}

	@Test
	void moveDown() {
		def piece = new Piece(PieceType.L, 0, 1, 2)
		assert piece.x == 1
		assert piece.y == 2

		def moved = piece.moveDown()
		assert moved.x == 1
		assert moved.y == 3

		// Check that the original does not change.
		assert piece.x == 1
		assert piece.y == 2
	}

	@Test
	void rotateLeft() {
		def piece = new Piece(PieceType.L, 0, 1, 2)
		assert piece.orientation == 0

		def rotated = piece.rotateLeft()
		assert rotated.orientation == 1
		assert piece.orientation == 0
	}

	@Test
	void rotateRight() {
		def piece = new Piece(PieceType.L, 0, 1, 2)
		assert piece.orientation == 0

		def rotated = piece.rotateRight()
		assert rotated.orientation == -1
		assert piece.orientation == 0
	}
}
