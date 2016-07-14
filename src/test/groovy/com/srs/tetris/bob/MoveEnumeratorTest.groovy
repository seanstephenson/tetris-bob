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

		def moves = new MoveEnumerator(board.toBitBoard(), piece).findPossibleMoves()

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

		def moves = new MoveEnumerator(board.toBitBoard(), piece).findPossibleMoves()

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

		def moves = new MoveEnumerator(board.toBitBoard(), piece).findPossibleMoves()

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

	List<GameBoard> boardsForMoves(GameBoard board, Piece piece, List<Move> moves) {
		return moves.collect {
			def placed = board.clone()
			placed.place(new Piece(piece.type, it.orientation, it.x, it.y))
			return placed
		}
	}
}
