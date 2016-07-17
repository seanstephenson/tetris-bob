package com.srs.tetris.bob.evaluator

import com.srs.tetris.bob.Position
import com.srs.tetris.game.BitBoard
import org.junit.Test

class HolesEvaluatorTest {
	@Test
	public void empty() {
		def evaluator = new HolesEvaluator()

		assert 0.0 == evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    . . .
		'''))).score
	}

	@Test
	public void zeroHoles() {
		def evaluator = new HolesEvaluator()

		assert 0.0 == evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    X . .
		    X . X
		'''))).score
	}

	@Test
	public void singleHole() {
		def evaluator = new HolesEvaluator()

		assert 1.0 == evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    X . .
		    . X .
		    X X .
		'''))).score
	}

	@Test
	public void twoHoles() {
		def evaluator = new HolesEvaluator()

		assert 2.0 == evaluator.evaluate(new Position(BitBoard.from('''
		    X . .
		    X . .
		    . X X
		    X X .
		'''))).score
	}

	@Test
	public void deepHole() {
		def evaluator = new HolesEvaluator()

		// A hole only counts as X no matter how deep it is.
		assert 1.0 == evaluator.evaluate(new Position(BitBoard.from('''
		    X . .
		    . X .
		    . X X
		    . X X
		'''))).score
	}
}
