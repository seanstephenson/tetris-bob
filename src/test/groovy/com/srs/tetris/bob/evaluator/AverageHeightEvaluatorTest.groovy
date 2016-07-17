package com.srs.tetris.bob.evaluator

import com.srs.tetris.bob.Position
import com.srs.tetris.game.BitBoard
import org.junit.Test

class AverageHeightEvaluatorTest {
	@Test
	public void evaluate() {
		def evaluator = new AverageHeightEvaluator()

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    . . .
		'''))).score == 0.0

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    . . X
		'''))).score == 1.0

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . X .
		    X . .
		'''))).score == 1.5

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . X .
		    X X X
		'''))).score == 1.25

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    X X X
		    . X .
		    X X X
		'''))).score == 2.0

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    X X X
		    X X X
		    X X X
		    X X X
		'''))).score == 2.5
	}
}
