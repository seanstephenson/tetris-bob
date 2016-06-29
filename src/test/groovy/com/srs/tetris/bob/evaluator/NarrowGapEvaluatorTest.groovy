package com.srs.tetris.bob.evaluator

import com.srs.tetris.game.Board
import org.junit.Test

class NarrowGapEvaluatorTest {
	@Test
	public void empty() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 0, 0],
		] as int[][]))

		assert score.twoGaps == 0
		assert score.threeGaps == 0
		assert score.fourGaps == 0
		assert score.score == 0
	}

	@Test
	public void noGaps() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [1, 0, 0],
		    [1, 1, 0],
		    [1, 1, 1],
		] as int[][]))

		assert score.twoGaps == 0
		assert score.threeGaps == 0
		assert score.fourGaps == 0
		assert score.score == 0
	}

	@Test
	public void single_twoGap() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [1, 0, 1],
		    [1, 0, 1],
		] as int[][]))

		assert score.twoGaps == 1
		assert score.threeGaps == 0
		assert score.fourGaps == 0
		assert score.score == 0.5
	}

	@Test
	public void double_twoGaps() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 0, 0],
		    [0, 1, 0],
		    [0, 1, 0],
		] as int[][]))

		assert score.twoGaps == 2
		assert score.threeGaps == 0
		assert score.fourGaps == 0
		assert score.score == 2
	}

	@Test
	public void single_threeGap() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 1, 0],
		    [0, 1, 1],
		    [0, 1, 1],
		] as int[][]))

		assert score.twoGaps == 0
		assert score.threeGaps == 1
		assert score.fourGaps == 0
		assert score.score == 1
	}

	@Test
	public void double_threeGap() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 0, 0],
		    [0, 1, 0],
		    [0, 1, 0],
		    [0, 1, 0],
		] as int[][]))

		assert score.twoGaps == 0
		assert score.threeGaps == 2
		assert score.fourGaps == 0
		assert score.score == 4
	}

	@Test
	public void single_fourGap() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 1, 0],
		    [0, 1, 1],
		    [0, 1, 1],
		    [0, 1, 1],
		] as int[][]))

		assert score.twoGaps == 0
		assert score.threeGaps == 0
		assert score.fourGaps == 1
		assert score.score == 3
	}

	@Test
	public void double_fourGap() {
		def evaluator = new NarrowGapEvaluator()

		def score = evaluator.evaluate(new Board([
		    [0, 1, 0],
		    [0, 1, 0],
		    [0, 1, 0],
		    [0, 1, 0],
		] as int[][]))

		assert score.twoGaps == 0
		assert score.threeGaps == 0
		assert score.fourGaps == 2
		assert score.score == 12
	}
}
