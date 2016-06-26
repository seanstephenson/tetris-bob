package com.srs.tetris.bob.evaluator

import com.srs.tetris.game.Board
import org.junit.Test

class HolesEvaluatorTest {
	@Test
	public void empty() {
		HolesEvaluator evaluator = new HolesEvaluator(10, 1)

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		] as int[][]))

		assert 0 == score.holes
		assert 0 == score.covers
		assert 0 == score.score
	}

	@Test
	public void zeroHoles() {
		HolesEvaluator evaluator = new HolesEvaluator(10, 1)

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [1, 0, 0],
		    [1, 0, 1],
		] as int[][]))

		assert 0 == score.holes
		assert 0 == score.covers
		assert 0 == score.score
	}

	@Test
	public void singleHole() {
		HolesEvaluator evaluator = new HolesEvaluator(10, 1)

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [1, 0, 0],
		    [0, 1, 0],
		    [1, 1, 0],
		] as int[][]))

		assert 1 == score.holes
		assert 1 == score.covers
		assert 11 == score.score
	}

	@Test
	public void twoHoles() {
		HolesEvaluator evaluator = new HolesEvaluator(10, 1)

		def score = evaluator.evaluate(new Board([
		    [1, 0, 0],
		    [1, 0, 0],
		    [0, 1, 1],
		    [1, 1, 0],
		] as int[][]))

		assert 2 == score.holes
		assert 3 == score.covers
		assert 23 == score.score
	}

	@Test
	public void deepHole() {
		HolesEvaluator evaluator = new HolesEvaluator(10, 1)

		// A hole only counts as 1 no matter how deep it is.
		def score = evaluator.evaluate(new Board([
		    [1, 0, 0],
		    [0, 1, 0],
		    [0, 1, 1],
		    [0, 1, 1],
		] as int[][]))

		assert 1 == score.holes
		assert 1 == score.covers
		assert 11 == score.score
	}

	@Test
	public void completedLine() {
		HolesEvaluator evaluator = new HolesEvaluator(10, 1)

		// Completed lines shouldn't count.
		def score = evaluator.evaluate(new Board([
		    [1, 1, 1],
		    [1, 0, 0],
		    [0, 1, 0],
		    [1, 1, 0],
		] as int[][]))

		assert 1 == score.holes
		assert 1 == score.covers
		assert 11 == score.score
	}
}
