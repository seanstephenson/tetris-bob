package com.srs.tetris.bob.evaluator

import com.srs.tetris.game.BitBoard
import org.junit.Test

class AverageHeightEvaluatorTest {
	@Test
	public void evaluate() {
		def evaluator = new AverageHeightEvaluator()

		assert evaluator.evaluate(BitBoard.from("""
		    . . .
		    . . .
		    . . .
		    . . .
		""")).score == 0.0

		assert evaluator.evaluate(BitBoard.from("""
		    . . .
		    . . .
		    . . .
		    . . X
		""")).score == 1.0

		assert evaluator.evaluate(BitBoard.from("""
		    . . .
		    . . .
		    . X .
		    X . .
		""")).score == 1.5

		assert evaluator.evaluate(BitBoard.from("""
		    . . .
		    . . .
		    . X .
		    X X X
		""")).score == 1.25

		assert evaluator.evaluate(BitBoard.from("""
		    . . .
		    X X X
		    . X .
		    X X X
		""")).score == 2.0

		assert evaluator.evaluate(BitBoard.from("""
		    X X X
		    X X X
		    X X X
		    X X X
		""")).score == 2.5
	}
}
