package com.srs.tetris.game

import org.junit.Test

class PieceTypeTest {
	@Test
	void board() {
		assert PieceType.I.board == BitBoard.from("""
			. . . .
			X X X X
			. . . .
			. . . .
		""")
		
		assert PieceType.I.getBoard(0) == BitBoard.from("""
			. . . .
			X X X X
			. . . .
			. . . .
		""")

		assert PieceType.I.getBoard(1) == BitBoard.from("""
			. X . .
			. X . .
			. X . .
			. X . .
		""")

		assert PieceType.I.getBoard(2) == BitBoard.from("""
			. . . .
			. . . .
			X X X X
			. . . .
		""")

		assert PieceType.I.getBoard(3) == BitBoard.from("""
			. . X .
			. . X .
			. . X .
			. . X .
		""")
	}

	@Test(expected = AssertionError.class)
	void board_invalid_negative() {
		PieceType.I.getBoard(-1);
	}

	@Test(expected = AssertionError.class)
	void board_invalid_large() {
		PieceType.I.getBoard(4);
	}
}
