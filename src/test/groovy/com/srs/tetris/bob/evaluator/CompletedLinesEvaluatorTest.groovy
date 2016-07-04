package com.srs.tetris.bob.evaluator

import com.srs.tetris.game.BitBoard
import org.junit.Test

class CompletedLinesEvaluatorTest {
	@Test
	public void evaluate() {
		def evaluator = new CompletedLinesEvaluator()

		assert evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		] as int[][])).score == 0.0

		assert evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [1, 1, 0],
		] as int[][])).score == 0.0

		assert evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 1, 0],
		    [1, 1, 0],
		] as int[][])).score == 0.0

		assert evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 1, 0],
		    [1, 1, 1],
		] as int[][])).score == 1.0

		assert evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
			[1, 1, 1],
		    [0, 1, 0],
		    [1, 1, 1],
		] as int[][])).score == 2.0

		assert evaluator.evaluate(new BitBoard([
			[1, 1, 1],
			[1, 1, 1],
			[1, 1, 1],
		    [1, 1, 1],
		] as int[][])).score == 4.0
	}
}
