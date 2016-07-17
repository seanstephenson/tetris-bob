package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;
import java.util.Arrays;
import java.util.List;

public class CompositeEvaluator implements PositionEvaluator {
	private List<PositionEvaluator> evaluators;

	public CompositeEvaluator(PositionEvaluator... evaluators) {
		this.evaluators = Arrays.asList(evaluators);
	}

	@Override
	public Score evaluate(Position position) {
		return new CompositeScore(
			evaluators.stream()
			.map((evaluator) -> evaluator.evaluate(position))
			.toArray(Score[]::new)
		);
	}
}
