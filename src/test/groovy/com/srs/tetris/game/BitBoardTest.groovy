package com.srs.tetris.game

import org.junit.Test

class BitBoardTest {
	@Test
	void copyBoard() {
		def board = new BitBoard(GameBoard.from('''
			. I .
			S . Z
			. L .
		'''))

		assert board == BitBoard.from('''
			. 1 .
			1 . 1
			. 1 .
		''')
	}

	@Test
	void rotateLeft_L() {
		def board = PieceType.L.board
		assert board == BitBoard.from('''
		    . . X
			X X X
			. . .
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			X X .
			. X .
			. X .
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			. . .
			X X X
			X . .
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			. X .
			. X .
			. X X
		''')
	}

	@Test
	void rotateLeft_bar() {
		def board = PieceType.I.board
		assert board == BitBoard.from('''
			. . . .
			X X X X
			. . . .
			. . . .
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			. X . .
			. X . .
			. X . .
			. X . .
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			. . . .
			. . . .
			X X X X
			. . . .
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			. . X .
			. . X .
			. . X .
			. . X .
		''')
	}

	@Test
	void rotateRight_bar() {
		def board = PieceType.I.board
		assert board == BitBoard.from('''
			. . . .
			X X X X
			. . . .
			. . . .
		''')

		assert (board = board.rotateRight()) == BitBoard.from('''
			. . X .
			. . X .
			. . X .
			. . X .
		''')

		assert (board = board.rotateRight()) == BitBoard.from('''
			. . . .
			. . . .
			X X X X
			. . . .
		''')

		assert (board = board.rotateRight()) == BitBoard.from('''
			. X . .
			. X . .
			. X . .
			. X . .
		''')
	}
	
	@Test
	void rotateLeft_nonSquare() {
		def board = BitBoard.from('''
			. . . .
			X X X X
		''')

		assert board == BitBoard.from('''
			. . . .
			X X X X
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			. X
			. X
			. X
			. X
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			X X X X
			. . . .
		''')

		assert (board = board.rotateLeft()) == BitBoard.from('''
			X .
			X .
			X .
			X .
		''')
	}

	@Test
	void rotateRight_nonSquare() {
		def board = BitBoard.from('''
			. . . .
			X X X X
		''')

		assert board == BitBoard.from('''
			. . . .
			X X X X
		''')

		assert (board = board.rotateRight()) == BitBoard.from('''
			X .
			X .
			X .
			X .
		''')

		assert (board = board.rotateRight()) == BitBoard.from('''
			X X X X
			. . . .
		''')

		assert (board = board.rotateRight()) == BitBoard.from('''
			. X
			. X
			. X
			. X
		''')
	}

	@Test
	void place() {
		def piece = PieceType.L.board
		def board = new BitBoard(4, 7)

		board.place(piece, 1, 1)
		assert board == BitBoard.from('''
			. . . .
			. . . X
			. X X X
			. . . .
			. . . .
			. . . .
			. . . .
		''')

		board.place(piece, 1, 4)
		assert board == BitBoard.from('''
			. . . .
			. . . X
			. X X X
			. . . .
			. . . X
			. X X X
			. . . .
		''')

		board.place(piece, 0, 0)
		assert board == BitBoard.from('''
			. . X .
			X X X X
			. X X X
			. . . .
			. . . X
			. X X X
			. . . .
		''')
	}

	@Test
	void place_walls() {
		def piece = PieceType.L.board
		def board = new BitBoard(4, 4)

		board.place(piece, 50, 50)
		assert board == BitBoard.from('''
			. . . .
			. . . .
			. . . .
			. . . .
		''')

		board.place(piece, -50, -50)
		assert board == BitBoard.from('''
			. . . .
			. . . .
			. . . .
			. . . .
		''')

		board.place(piece, -2, -1)
		assert board == BitBoard.from('''
			X . . .
			. . . .
			. . . .
			. . . .
		''')

		board.place(piece, 2, -1)
		assert board == BitBoard.from('''
			X . X X
			. . . .
			. . . .
			. . . .
		''')

		board.place(piece, 1, 3)
		assert board == BitBoard.from('''
			X . X X
			. . . .
			. . . .
			. . . X
		''')
	}

	@Test
	void canPlace() {
		def piece = PieceType.L.board
		def board = new BitBoard(4, 4)

		assert board.canPlace(piece, 0, 0)

		board.place(piece, 0, 0)
		assert board == BitBoard.from('''
			. . X .
			X X X .
			. . . .
			. . . .
		''')

		assert !board.canPlace(piece, 0, 0)
		assert !board.canPlace(piece, 0, 1)
		assert !board.canPlace(piece, 1, 0)

		assert board.canPlace(piece, 1, 1)
	}

	@Test
	void canPlace_walls() {
		def piece = PieceType.L.board
		def board = new BitBoard(4, 4)

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
		def board = BitBoard.from('''
			. . .
			. X .
			X X .
		''')

		assert !board.isColumnEmpty(0)
		assert !board.isColumnEmpty(1)
		assert board.isColumnEmpty(2)
	}

	@Test
	void isLineEmpty() {
		def board = BitBoard.from('''
			. . . .
			. . X X
			X X X X
		''')

		assert board.isLineEmpty(0)
		assert !board.isLineEmpty(1)
		assert !board.isLineEmpty(2)
	}

	@Test
	void isLineComplete() {
		def board = BitBoard.from('''
			. . . .
			. . X X
			X X X X
		''')

		assert !board.isLineComplete(0)
		assert !board.isLineComplete(1)
		assert board.isLineComplete(2)
	}

	@Test
	void removeLine() {
		def board = BitBoard.from('''
			. . . X
			. . X X
			X X X X
			X X X X
		''')

		assert board == BitBoard.from('''
			. . . X
			. . X X
			X X X X
			X X X X
		''')

		board.removeLine(2)
		assert board == BitBoard.from('''
			. . . .
			. . . X
			. . X X
			X X X X
		''')

		board.removeLine(0)
		assert board == BitBoard.from('''
			. . . .
			. . . X
			. . X X
			X X X X
		''')

		board.removeLine(3)
		assert board == BitBoard.from('''
			. . . .
			. . . .
			. . . X
			. . X X
		''')
	}

	@Test
	void removeCompleteLines() {
		def board = BitBoard.from('''
			. . . X
			. . X X
			X X X X
			X X X X
		''')

		assert 2 == board.removeCompleteLines()
		assert board == BitBoard.from('''
			. . . .
			. . . .
			. . . X
			. . X X
		''')
	}
}
