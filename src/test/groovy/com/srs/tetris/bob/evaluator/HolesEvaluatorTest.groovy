package com.srs.tetris.bob.evaluator

import com.srs.tetris.game.BitBoard
import org.junit.Test

class HolesEvaluatorTest {
	@Test
	public void empty() {
		def evaluator = new HolesEvaluator()

		assert 0 == evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		] as int[][])).score
	}

	@Test
	public void zeroHoles() {
		def evaluator = new HolesEvaluator()

		assert 0 == evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
		    [0, 0, 0],
		    [1, 0, 0],
		    [1, 0, 1],
		] as int[][])).score
	}

	@Test
	public void singleHole() {
		def evaluator = new HolesEvaluator()

		assert 1 == evaluator.evaluate(new BitBoard([
		    [0, 0, 0],
		    [1, 0, 0],
		    [0, 1, 0],
		    [1, 1, 0],
		] as int[][])).score
	}

	@Test
	public void twoHoles() {
		def evaluator = new HolesEvaluator()

		assert 2 == evaluator.evaluate(new BitBoard([
		    [1, 0, 0],
		    [1, 0, 0],
		    [0, 1, 1],
		    [1, 1, 0],
		] as int[][])).score
	}

	@Test
	public void deepHole() {
		def evaluator = new HolesEvaluator()

		// A hole only counts as 1 no matter how deep it is.
		assert 1 == evaluator.evaluate(new BitBoard([
		    [1, 0, 0],
		    [0, 1, 0],
		    [0, 1, 1],
		    [0, 1, 1],
		] as int[][])).score
	}
}
