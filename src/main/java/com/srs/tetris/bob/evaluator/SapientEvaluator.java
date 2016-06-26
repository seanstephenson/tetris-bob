package com.srs.tetris.bob.evaluator;

/**
 * The best known combination of evaluators.
 */
public class SapientEvaluator extends CompositeEvaluator {
	private static final double HEIGHT_WEIGHT = -1.0;
	private static final double COMPLETED_LINES_WEIGHT = 2.0;
	private static final double HOLE_WEIGHT = -10.0;
	private static final double HOLE_COVER_WEIGHT = -2.0;

	public SapientEvaluator() {
		super(
			new WeightedEvaluator(new HeightEvaluator(), HEIGHT_WEIGHT),
			new WeightedEvaluator(new CompletedLinesEvaluator(), COMPLETED_LINES_WEIGHT),
			new HolesEvaluator(HOLE_WEIGHT, HOLE_COVER_WEIGHT)
		);
	}
}
