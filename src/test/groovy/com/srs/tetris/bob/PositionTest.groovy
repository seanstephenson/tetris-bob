package com.srs.tetris.bob

import com.srs.tetris.game.BitBoard
import com.srs.tetris.game.Piece
import com.srs.tetris.game.PieceType
import org.junit.Test

class PositionTest {
	@Test
	void doMove() {
		def position = new Position(BitBoard.from('''
			. . .
			X . .
			X X .
		'''), PieceType.O, [PieceType.I, PieceType.S], null, false);

		position = position.doMove(new Move(new Piece(PieceType.O, 0, 1, 0)))

		assert position.board == BitBoard.from('''
			. . .
			. X X
			X X .
		''')

		assert position.piece == PieceType.I
		assert position.nextPieces == [PieceType.S]
		assert position.swapPiece == null
		assert !position.pieceSwapped
	}

	@Test
	void doMove_lastNext() {
		def position = new Position(BitBoard.from('''
			. . .
			X . .
			X X .
		'''), PieceType.O, [PieceType.S], null, false);

		position = position.doMove(new Move(new Piece(PieceType.O, 0, 1, 0)))

		assert position.board == BitBoard.from('''
			. . .
			. X X
			X X .
		''')

		assert position.piece == PieceType.S
		assert position.nextPiece == null
		assert position.nextPieces == []
		assert position.swapPiece == null
		assert !position.pieceSwapped
	}

	@Test
	void doMove_noNext() {
		def position = new Position(BitBoard.from('''
			. . .
			X . .
			X X .
		'''), PieceType.O, [], null, false);

		position = position.doMove(new Move(new Piece(PieceType.O, 0, 1, 0)))

		assert position.board == BitBoard.from('''
			. . .
			. X X
			X X .
		''')

		assert position.piece == null
		assert position.nextPiece == null
		assert position.nextPieces == []
		assert position.swapPiece == null
		assert !position.pieceSwapped
	}

	@Test
	void doMove_swap() {
		def position = new Position(BitBoard.from('''
			. . .
			X . .
			X X .
		'''), PieceType.O, [PieceType.I, PieceType.S], PieceType.L, false);

		position = position.doMove(Move.swap())

		// The board should be unchanged.
		assert position.board == BitBoard.from('''
			. . .
			X . .
			X X .
		''')

		assert position.piece == PieceType.L
		assert position.nextPieces == [PieceType.I, PieceType.S]
		assert position.swapPiece == PieceType.O
		assert position.pieceSwapped
	}
}
