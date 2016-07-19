package com.srs.tetris.bob.evaluator;

import com.srs.tetris.bob.Position;

/**
 * Wraps another evaluator in order to measure the number of positions evaluated.
 */
public class CountingPositionEvaluator implements PositionEvaluator {
	private PositionEvaluator wrapped;
	private long positionsEvaluated;

	public CountingPositionEvaluator(PositionEvaluator wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public Score evaluate(Position position) {
		positionsEvaluated++;
		return wrapped.evaluate(position);
	}

	public long getPositionsEvaluated() {
		return positionsEvaluated;
	}
}
