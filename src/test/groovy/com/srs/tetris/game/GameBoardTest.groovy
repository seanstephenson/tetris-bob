package com.srs.tetris.game

import org.junit.Test

class GameBoardTest {
	@Test
	void place() {
		def piece = new Piece(PieceType.L)
		def board = new GameBoard(4, 7)
		
		board.place(piece.moveTo(1, 1))
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 6],
			[0, 6, 6, 6],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece.moveTo(1, 4))
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 6],
			[0, 6, 6, 6],
			[0, 0, 0, 0],
			[0, 0, 0, 6],
			[0, 6, 6, 6],
			[0, 0, 0, 0],
		]

		board.place(piece.moveTo(0, 0))
		assert board.grid == [
			[0, 0, 6, 0],
			[6, 6, 6, 6],
			[0, 6, 6, 6],
			[0, 0, 0, 0],
			[0, 0, 0, 6],
			[0, 6, 6, 6],
			[0, 0, 0, 0],
		]
	}

	@Test
	void place_walls() {
		def piece = new Piece(PieceType.L)
		def board = new GameBoard(4, 4)

		board.place(piece.moveTo(50, 50))
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece.moveTo(-50, -50))
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece.moveTo(-2, -1))
		assert board.grid == [
			[6, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece.moveTo(2, -1))
		assert board.grid == [
			[6, 0, 6, 6],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece.moveTo(1, 3))
		assert board.grid == [
			[6, 0, 6, 6],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 6],
		]
	}

	@Test
	void canPlace() {
		def piece = new Piece(PieceType.L)
		def board = new GameBoard(4, 4)

		assert board.canPlace(piece.moveTo(0, 0))

		board.place(piece.moveTo(0, 0))
		assert board.grid == [
			[0, 0, 6, 0],
			[6, 6, 6, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		assert !board.canPlace(piece.moveTo(0, 0))
		assert !board.canPlace(piece.moveTo(0, 1))
		assert !board.canPlace(piece.moveTo(1, 0))

		assert board.canPlace(piece.moveTo(1, 1))
	}

	@Test
	void canPlace_walls() {
		def piece = new Piece(PieceType.L)
		def board = new GameBoard(4, 4)

		assert board.canPlace(piece.moveTo(0, 0))
		assert !board.canPlace(piece.moveTo(-1, 0))
		assert !board.canPlace(piece.moveTo(0, -1))

		assert board.canPlace(piece.moveTo(1, 0))
		assert !board.canPlace(piece.moveTo(2, 0))

		assert board.canPlace(piece.moveTo(0, 2))
		assert !board.canPlace(piece.moveTo(0, 3))
	}

	@Test
	void isColumnEmpty() {
		def board = new GameBoard([
			[0, 0, 0],
			[0, 1, 0],
			[1, 1, 0],
		] as int[][])

		assert !board.isColumnEmpty(0)
		assert !board.isColumnEmpty(1)
		assert board.isColumnEmpty(2)
	}

	@Test
	void isLineEmpty() {
		def board = new GameBoard([
			[0, 0, 0, 0],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		] as int[][])

		assert board.isLineEmpty(0)
		assert !board.isLineEmpty(1)
		assert !board.isLineEmpty(2)
	}

	@Test
	void isLineComplete() {
		def board = new GameBoard([
			[0, 0, 0, 0],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		] as int[][])

		assert !board.isLineComplete(0)
		assert !board.isLineComplete(1)
		assert board.isLineComplete(2)
	}

	@Test
	void removeLine() {
		def board = new GameBoard([
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
			[1, 1, 1, 1],
		] as int[][])

		assert board.grid == [
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
			[1, 1, 1, 1],
		]

		board.removeLine(2)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		]

		board.removeLine(0)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		]

		board.removeLine(3)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 0, 1, 1],
		]
	}
}
