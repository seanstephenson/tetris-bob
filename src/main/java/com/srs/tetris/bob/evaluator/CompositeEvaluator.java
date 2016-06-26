package com.srs.tetris.bob.evaluator;

import com.srs.tetris.game.Board;
import java.util.Arrays;
import java.util.List;

public class CompositeEvaluator implements BoardEvaluator {
	private List<BoardEvaluator> evaluators;

	public CompositeEvaluator(BoardEvaluator... evaluators) {
		this.evaluators = Arrays.asList(evaluators);
	}

	@Override
	public Score evaluate(Board board) {
		return new CompositeScore(
			evaluators.stream()
			.map((evaluator) -> evaluator.evaluate(board))
			.toArray(Score[]::new)
		);
	}
}
