package com.srs.tetris.bob.evaluator

import com.srs.tetris.game.BitBoard
import org.junit.Test

class HeightEvaluatorTest {
	@Test
	public void evaluate() {
		def evaluator = new HeightEvaluator()

		assert evaluator.evaluate(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    . . .
		''')).score == 0.0

		assert evaluator.evaluate(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    . . X
		''')).score == 1.0

		assert evaluator.evaluate(BitBoard.from('''
		    X . .
		    . X .
		    . X .
		    X X X
		''')).score == 4.0
	}
}
