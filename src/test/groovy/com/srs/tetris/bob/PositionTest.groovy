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
	void doMove_swap() {
		def position = new Position(BitBoard.from('''
			. . .
			X . .
			X X .
		'''), PieceType.O, [PieceType.I, PieceType.S], PieceType.L, false);

		position = position.doMove(new Move(new Piece(PieceType.L, 1, 1, 0)))

		assert position.board == BitBoard.from('''
			. . .
			. X X
			X . X
		''')

		assert position.piece == PieceType.I
		assert position.nextPieces == [PieceType.S]
		assert position.swapPiece == PieceType.O
		assert position.pieceSwapped
	}
}
