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

	@Test
	void isColumnEmpty() {
		def board = new Board([
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
		def board = new Board([
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
		def board = new Board([
			[0, 0, 0, 0],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		] as int[][])

		assert !board.isLineComplete(0)
		assert !board.isLineComplete(1)
		assert board.isLineComplete(2)
	}

	@Test
	void fillLine() {
		def board = new Board([
			[0, 0, 0, 0],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		] as int[][])

		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		]

		board.fillLine(0, 2)
		assert board.grid == [
			[2, 2, 2, 2],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
		]

		board.fillLine(1, 0)
		assert board.grid == [
			[2, 2, 2, 2],
			[0, 0, 0, 0],
			[1, 1, 1, 1],
		]

		board.fillLine(2, 3)
		assert board.grid == [
			[2, 2, 2, 2],
			[0, 0, 0, 0],
			[3, 3, 3, 3],
		]
	}

	@Test
	void removeLine() {
		def board = new Board([
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
			[2, 2, 2, 2],
		] as int[][])

		assert board.grid == [
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[1, 1, 1, 1],
			[2, 2, 2, 2],
		]

		board.removeLine(2)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[2, 2, 2, 2],
		]

		board.removeLine(0)
		assert board.grid == [
			[0, 0, 0, 0],
			[0, 0, 0, 1],
			[0, 0, 1, 1],
			[2, 2, 2, 2],
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
