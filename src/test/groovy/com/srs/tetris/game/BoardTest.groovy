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
	public void place() {
		def piece = PieceType.L.board
		def board = new Board(4, 7)

		board = board.place(piece, 1, 1)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 1, 1, 1],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
			[0, 0, 0, 0],
		]

		board = board.place(piece, 1, 4)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 1, 1, 1],
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 1, 1, 1],
			[0, 0, 0, 0],
		]
	}

	@Test
	public void place_immutable() {
		def piece = PieceType.L.board
		def board = new Board(3, 3)

		// Placing a piece shouldn't change the original board
		def placedBoard = board.place(piece, 0, 0)
		assert placedBoard.grid == [
			[0, 0, 1],
			[1, 1, 1],
			[0, 0, 0],
		]

		assert board.grid == [
			[0, 0, 0],
			[0, 0, 0],
			[0, 0, 0],
		]
	}

	@Test
	public void canPlace() {
		def piece = PieceType.L.board
		def board = new Board(4, 4)

		assert board.canPlace(piece, 0, 0)

		board = board.place(piece, 0, 0)
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
}
