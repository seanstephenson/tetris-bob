package com.srs.tetris.bob.evaluator

import com.srs.tetris.game.Board
import org.junit.Test

class DangerZoneEvaluatorTest {
	@Test
	public void evaluate() {
		def evaluator = new DangerZoneEvaluator(3, 2.0)

		assert evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		] as int[][])).score == 0.0

		assert evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [1, 0, 0],
		    [1, 0, 1],
		] as int[][])).score == 0.0

		assert evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [1, 0, 0],
		    [1, 0, 0],
		    [1, 0, 1],
		] as int[][])).score == 1.0

		assert evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [1, 0, 0],
		    [1, 0, 0],
		    [1, 0, 0],
		    [1, 0, 1],
		] as int[][])).score == 4.0

		assert evaluator.evaluate(new Board([
		    [1, 0, 0],
		    [1, 0, 0],
		    [1, 0, 0],
		    [1, 0, 0],
		    [1, 0, 1],
		] as int[][])).score == 9.0
	}
}
