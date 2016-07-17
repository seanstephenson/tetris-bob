package com.srs.tetris.bob.evaluator

import com.srs.tetris.bob.Position
import com.srs.tetris.game.BitBoard
import org.junit.Test

class NarrowGapEvaluatorTest {

	def evaluator = new NarrowGapEvaluator(1, 10, 100)

	@Test
	public void empty() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . . .
		    . . .
		''')))

		assert score.score == 0
	}

	@Test
	public void noGaps() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    X . .
		    X X .
		    X X X
		''')))

		assert score.score == 0
	}

	@Test
	public void single_twoGap() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    X . X
		    X . X
		''')))

		assert score.score == 1
	}

	@Test
	public void double_twoGaps() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . . .
		    . X .
		    . X .
		''')))

		assert score.score == 2
	}

	@Test
	public void single_threeGap() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . X .
		    . X X
		    . X X
		''')))

		assert score.score == 10
	}

	@Test
	public void double_threeGap() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    . X .
		    . X .
		    . X .
		''')))

		assert score.score == 20
	}

	@Test
	public void single_fourGap() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . X .
		    . X X
		    . X X
		    . X X
		''')))

		assert score.score == 100
	}

	@Test
	public void double_fourGap() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . X .
		    . X .
		    . X .
		    . X .
		''')))

		assert score.score == 200
	}

	@Test
	public void coveredGap_wall() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    X X .
		    . X X
		    . X X
		''')))

		assert score.score == 1
	}

	@Test
	public void coveredGap_middle() {
		def score = evaluator.evaluate(new Position(BitBoard.from('''
		    . . .
		    X X .
		    X . X
		    X . X
		''')))

		assert score.score == 1
	}
}
