package com.srs.tetris.game

import org.junit.Test

class BoardTest {
	@Test
	void rotateLeft_L() {
		def board = PieceType.L.board
		assert board.grid == [
		    [0, 0, 1],
			[1, 1, 1],
			[0, 0, 0],
		]

		assert (board = board.rotateLeft()).grid == [
			[1, 1, 0],
			[0, 1, 0],
			[0, 1, 0],
		]

		assert (board = board.rotateLeft()).grid == [
			[0, 0, 0],
			[1, 1, 1],
			[1, 0, 0],
		]

		assert (board = board.rotateLeft()).grid == [
			[0, 1, 0],
			[0, 1, 0],
			[0, 1, 1],
		]
	}

	@Test
	void rotateLeft_bar() {
		def board = PieceType.Bar.board
		assert board.grid == [
			[0, 0, 0, 0],
			[1, 1, 1, 1],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		assert (board = board.rotateLeft()).grid == [
			[0, 1, 0, 0],
			[0, 1, 0, 0],
			[0, 1, 0, 0],
			[0, 1, 0, 0],
		]

		assert (board = board.rotateLeft()).grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[1, 1, 1, 1],
			[0, 0, 0, 0],
		]

		assert (board = board.rotateLeft()).grid == [
			[0, 0, 1, 0],
			[0, 0, 1, 0],
			[0, 0, 1, 0],
			[0, 0, 1, 0],
		]
	}

	@Test
	void rotateRight_bar() {
		def board = PieceType.Bar.board
		assert board.grid == [
			[0, 0, 0, 0],
			[1, 1, 1, 1],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		assert (board = board.rotateRight()).grid == [
			[0, 0, 1, 0],
			[0, 0, 1, 0],
			[0, 0, 1, 0],
			[0, 0, 1, 0],
		]

		assert (board = board.rotateRight()).grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[1, 1, 1, 1],
			[0, 0, 0, 0],
		]

		assert (board = board.rotateRight()).grid == [
			[0, 1, 0, 0],
			[0, 1, 0, 0],
			[0, 1, 0, 0],
			[0, 1, 0, 0],
		]
	}
	
	@Test
	void rotateLeft_nonSquare() {
		def board = new Board([
			[0, 0, 0, 0],
			[1, 1, 1, 1],
		] as int[][])

		assert board.grid == [
			[0, 0, 0, 0],
			[1, 1, 1, 1],
		]

		assert (board = board.rotateLeft()).grid == [
			[0, 1],
			[0, 1],
			[0, 1],
			[0, 1],
		]

		assert (board = board.rotateLeft()).grid == [
			[1, 1, 1, 1],
			[0, 0, 0, 0],
		]

		assert (board = board.rotateLeft()).grid == [
			[1, 0],
			[1, 0],
			[1, 0],
			[1, 0],
		]
	}

	@Test
	void rotateRight_nonSquare() {
		def board = new Board([
			[0, 0, 0, 0],
			[1, 1, 1, 1],
		] as int[][])

		assert board.grid == [
			[0, 0, 0, 0],
			[1, 1, 1, 1],
		]

		assert (board = board.rotateRight()).grid == [
			[1, 0],
			[1, 0],
			[1, 0],
			[1, 0],
		]

		assert (board = board.rotateRight()).grid == [
			[1, 1, 1, 1],
			[0, 0, 0, 0],
		]

		assert (board = board.rotateRight()).grid == [
			[0, 1],
			[0, 1],
			[0, 1],
			[0, 1],
		]
	}

	@Test
	void place() {
		def piece = PieceType.L.board
		def board = new Board(4, 7)

		board.place(piece, 1, 1, 1)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 1, 1, 1],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece, 1, 4, 2)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 1, 1, 1],
			[0, 0, 0, 0],
			[0, 0, 0, 2],
			[0, 2, 2, 2],
			[0, 0, 0, 0],
		]

		board.place(piece, 0, 0, 3)
		assert board.grid == [
			[0, 0, 3, 0],
			[3, 3, 3, 1],
			[0, 1, 1, 1],
			[0, 0, 0, 0],
			[0, 0, 0, 2],
			[0, 2, 2, 2],
			[0, 0, 0, 0],
		]
	}

	@Test
	void place_walls() {
		def piece = PieceType.L.board
		def board = new Board(4, 4)

		board.place(piece, 50, 50, 1)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece, -50, -50, 1)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece, -2, -1, 1)
		assert board.grid == [
			[1, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece, 2, -1, 2)
		assert board.grid == [
			[1, 0, 2, 2],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board.place(piece, 1, 3, 3)
		assert board.grid == [
			[1, 0, 2, 2],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 3],
		]
	}

	@Test
	void canPlace() {
		def piece = PieceType.L.board
		def board = new Board(4, 4)

		assert board.canPlace(piece, 0, 0)

		board.place(piece, 0, 0, 1)
		assert board.grid == [
			[0, 0, 1, 0],
			[1, 1, 1, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		assert !board.canPlace(piece, 0, 0)
		assert !board.canPlace(piece, 0, 1)
		assert !board.canPlace(piece, 1, 0)

		assert board.canPlace(piece, 1, 1)
	}

	@Test
	void canPlace_walls() {
		def piece = PieceType.L.board
		def board = new Board(4, 4)

		assert board.canPlace(piece, 0, 0)
		assert !board.canPlace(piece, -1, 0)
		assert !board.canPlace(piece, 0, -1)

		assert board.canPlace(piece, 1, 0)
		assert !board.canPlace(piece, 2, 0)

		assert board.canPlace(piece, 0, 2)
		assert !board.canPlace(piece, 0, 3)
	}
}
