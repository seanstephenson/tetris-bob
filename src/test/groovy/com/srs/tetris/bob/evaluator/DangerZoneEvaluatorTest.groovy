package com.srs.tetris.bob.evaluator

import com.srs.tetris.bob.Position
import com.srs.tetris.game.BitBoard
import org.junit.Test

class DangerZoneEvaluatorTest {
	@Test
	public void evaluate() {
		def evaluator = new DangerZoneEvaluator(3, 2.0)

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    . . .
		    . . .
		'''))).score == 0.0

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    X . .
		    X . X
		'''))).score == 0.0

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    X . .
		    X . .
		    X . X
		'''))).score == 1.0

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    X . .
		    X . .
		    X . .
		    X . X
		'''))).score == 4.0

		assert evaluator.evaluate(new Position(BitBoard.from('''
		    X . .
		    X . .
		    X . .
		    X . .
		    X . X
		'''))).score == 9.0
	}
}
