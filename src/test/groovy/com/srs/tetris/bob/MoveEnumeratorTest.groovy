package com.srs.tetris.bob

import com.srs.tetris.game.GameBoard
import com.srs.tetris.game.Piece
import com.srs.tetris.game.PieceType
import org.junit.Test

class MoveEnumeratorTest {
	@Test
	void findPossibleMoves_O() {
		def piece = new Piece(PieceType.O)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		def moves = new MoveEnumerator().findPossibleMoves(new Position(board.toBitBoard(), piece))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . . .
				O O . .
				O O T .
				T . T T
			"""),
			GameBoard.from("""
				. O O .
				. O O .
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. . O O
				. . O O
				. . T .
				T . T T
			"""),
		]
	}

	@Test
	void findPossibleMoves_I() {
		def piece = new Piece(PieceType.I)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		def moves = new MoveEnumerator().findPossibleMoves(new Position(board.toBitBoard(), piece))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . . .
				I I I I
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. I . .
				. I . .
				. I T .
				T I T T
			"""),
		]
	}

	@Test
	void findPossibleMoves_L() {
		def piece = new Piece(PieceType.L)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		def moves = new MoveEnumerator().findPossibleMoves(new Position(board.toBitBoard(), piece))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . L .
				L L L .
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. . . L
				. L L L
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. . . .
				L L . .
				. L T .
				T L T T
			"""),
			GameBoard.from("""
				. . L L
				. . . L
				. . T L
				T . T T
			"""),
			GameBoard.from("""
				. . . .
				L L L .
				L . T .
				T . T T
			"""),
			GameBoard.from("""
				. . . .
				. L L L
				. L T .
				T . T T
			"""),
			GameBoard.from("""
				L . . .
				L . . .
				L L T .
				T . T T
			"""),
		]
	}

	@Test
	void findPossibleMoves_swap() {
		def piece = new Piece(PieceType.I)
		def swapPiece = new Piece(PieceType.O)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		// Moves for both the current piece (I) and the swap piece (O) will be generated.
		def moves = new MoveEnumerator().findPossibleMoves(new Position(board.toBitBoard(), piece, null, swapPiece, false))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . . .
				I I I I
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. I . .
				. I . .
				. I T .
				T I T T
			"""),
			GameBoard.from("""
				. . . .
				O O . .
				O O T .
				T . T T
			"""),
			GameBoard.from("""
				. O O .
				. O O .
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. . O O
				. . O O
				. . T .
				T . T T
			"""),
		]
	}

	@Test
	void findPossibleMoves_swap_noSwapPiece() {
		def piece = new Piece(PieceType.I)
		def nextPiece = new Piece(PieceType.O)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		// Because there is no swap piece, swap moves for the next piece will be generated.
		def moves = new MoveEnumerator().findPossibleMoves(new Position(board.toBitBoard(), piece, nextPiece, null, false))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . . .
				I I I I
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. I . .
				. I . .
				. I T .
				T I T T
			"""),
			GameBoard.from("""
				. . . .
				O O . .
				O O T .
				T . T T
			"""),
			GameBoard.from("""
				. O O .
				. O O .
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. . O O
				. . O O
				. . T .
				T . T T
			"""),
		]
	}

	@Test
	void findPossibleMoves_swap_samePieceType() {
		def piece = new Piece(PieceType.I)
		def swapPiece = new Piece(PieceType.I)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		// Because the swap piece and the piece in play are the same, no swap moves will be generated.
		def moves = new MoveEnumerator().findPossibleMoves(new Position(board.toBitBoard(), piece, null, swapPiece, false))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . . .
				I I I I
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. I . .
				. I . .
				. I T .
				T I T T
			"""),
		]
	}

	@Test
	void findPossibleMoves_swap_pieceAlreadySwapped() {
		def piece = new Piece(PieceType.I)
		def swapPiece = new Piece(PieceType.O)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		// Because the piece has already been swapped, no swap moves will be generated.
		def moves = new MoveEnumerator().findPossibleMoves(new Position(board.toBitBoard(), piece, null, swapPiece, true))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . . .
				I I I I
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. I . .
				. I . .
				. I T .
				T I T T
			"""),
		]
	}

	@Test
	void findPossibleMoves_swap_allowSwapFalse() {
		def piece = new Piece(PieceType.I)
		def swapPiece = new Piece(PieceType.O)
		def board = GameBoard.from("""
			. . . .
			. . . .
			. . T .
			T . T T
		""")

		// Because allowSwap is set to false, no swap moves will be generated.
		def moveEnumerator = new MoveEnumerator(allowSwap: false)
		def moves = moveEnumerator.findPossibleMoves(new Position(board.toBitBoard(), piece, null, swapPiece, false))

		assert boardsForMoves(board, piece, moves) == [
			GameBoard.from("""
				. . . .
				I I I I
				. . T .
				T . T T
			"""),
			GameBoard.from("""
				. I . .
				. I . .
				. I T .
				T I T T
			"""),
		]
	}

	List<GameBoard> boardsForMoves(GameBoard board, Piece piece, List<Move> moves) {
		return moves.collect {
			def placed = board.clone()
			placed.place(it.piece)
			return placed
		}
	}
}
