package com.srs.tetris.bob

import com.srs.tetris.game.BitBoard
import com.srs.tetris.game.Piece
import com.srs.tetris.game.PieceType
import org.junit.Test

class MoveEnumeratorTest {
	@Test
	void findPossibleMoves_O() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.O)
		
		def moves = new MoveEnumerator().findPossibleMoves(position)

		assert boardsForMoves(position, moves) == [
			BitBoard.from('''
				. . . .
				O O . .
				O O X .
				X . X X
			'''),
			BitBoard.from('''
				. O O .
				. O O .
				. . X .
				X . X X
			'''),
			BitBoard.from('''
				. . O O
				. . O O
				. . X .
				X . X X
			'''),
		]
	}

	@Test
	void findPossibleMoves_I() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.I)

		def moves = new MoveEnumerator().findPossibleMoves(position)

		assert boardsForMoves(position, moves) == [
			BitBoard.from('''
				. . . .
				I I I I
				. . X .
				X . X X
			'''),
			BitBoard.from('''
				. I . .
				. I . .
				. I X .
				X I X X
			'''),
		]
	}

	@Test
	void findPossibleMoves_L() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.L)

		def moves = new MoveEnumerator().findPossibleMoves(position)

		assert boardsForMoves(position, moves) == [
			BitBoard.from('''
				. . L .
				L L L .
				. . X .
				X . X X
			'''),
			BitBoard.from('''
				. . . L
				. L L L
				. . X .
				X . X X
			'''),
			BitBoard.from('''
				. . . .
				L L . .
				. L X .
				X L X X
			'''),
			BitBoard.from('''
				. . L L
				. . . L
				. . X L
				X . X X
			'''),
			BitBoard.from('''
				. . . .
				L L L .
				L . X .
				X . X X
			'''),
			BitBoard.from('''
				. . . .
				. L L L
				. L X .
				X . X X
			'''),
			BitBoard.from('''
				L . . .
				L . . .
				L L X .
				X . X X
			'''),
		]
	}

	@Test
	void findPossibleMoves_swap() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.I, [], PieceType.O, false)

		// A swap move should be generated
		def moves = new MoveEnumerator().findPossibleMoves(position)
		assert moves.size == 3
		assert moves.findAll { it.swap }.size() == 1
	}

	@Test
	void findPossibleMoves_swap_noSwapPiece() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.I, [], PieceType.O, false)

		// There's no swap piece, but there is a next piece, so we can still swap
		def moves = new MoveEnumerator().findPossibleMoves(position)
		assert moves.size == 3
		assert moves.findAll { it.swap }.size() == 1
	}

	@Test
	void findPossibleMoves_swap_samePieceType() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.I, [], PieceType.I, false)

		// Because the swap piece and the piece in play are the same, no swap moves will be generated.
		def moves = new MoveEnumerator().findPossibleMoves(position)
		assert moves.size == 2
		assert moves.findAll { it.swap }.size() == 0
	}

	@Test
	void findPossibleMoves_swap_samePieceTypeAsNextPiece() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.I, [PieceType.I], null, false)

		// Because there is no swap piece and the next piece and piece in play are the same, no swap moves will be generated.
		def moves = new MoveEnumerator().findPossibleMoves(position)
		assert moves.size == 2
		assert moves.findAll { it.swap }.size() == 0
	}

	@Test
	void findPossibleMoves_swap_pieceAlreadySwapped() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.I, [], PieceType.O, true)

		// Because the piece has already been swapped, no swap moves will be generated.
		def moves = new MoveEnumerator().findPossibleMoves(position)
		assert moves.size == 2
		assert moves.findAll { it.swap }.size() == 0
	}

	@Test
	void findPossibleMoves_swap_allowSwapFalse() {
		def position = new Position(BitBoard.from('''
			. . . .
			. . . .
			. . X .
			X . X X
		'''), PieceType.I, [], PieceType.O, false)

		// Because allowSwap is set to false, no swap moves will be generated.
		def moveEnumerator = new MoveEnumerator(allowSwap: false)
		def moves = moveEnumerator.findPossibleMoves(position)
		assert moves.size == 2
		assert moves.findAll { it.swap }.size() == 0
	}

	List<BitBoard> boardsForMoves(Position position, List<Move> moves) {
		return moves.collect {
			BitBoard board = position.board.clone()
			board.place(it.piece)
			return board
		}
	}
}
