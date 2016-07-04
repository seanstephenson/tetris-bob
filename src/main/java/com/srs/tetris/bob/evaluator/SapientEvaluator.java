package com.srs.tetris.bob.evaluator;

/**
 * The best known combination of evaluators.
 */
public class SapientEvaluator extends CompositeEvaluator {
	private static final double HEIGHT_WEIGHT = -1.0;
	private static final double AVERAGE_HEIGHT_WEIGHT = -1.0;

	private static final double DANGER_ZONE_WEIGHT = -4.0;
	private static final double DANGER_ZONE_EXPONENT = 1.25;
	private static final int DANGER_ZONE_SIZE = 10;

	private static final double HOLE_WEIGHT = -10.0;
	private static final double HOLE_COVER_WEIGHT = -2.0;
	private static final double NARROW_GAP_WEIGHT = -1.0;

	public SapientEvaluator() {
		super(
			new WeightedEvaluator(new HeightEvaluator(), HEIGHT_WEIGHT),
			new WeightedEvaluator(new AverageHeightEvaluator(), AVERAGE_HEIGHT_WEIGHT),
			new WeightedEvaluator(new DangerZoneEvaluator(DANGER_ZONE_SIZE, DANGER_ZONE_EXPONENT), DANGER_ZONE_WEIGHT),
			new HolesEvaluator(HOLE_WEIGHT, HOLE_COVER_WEIGHT),
			new WeightedEvaluator(new NarrowGapEvaluator(), NARROW_GAP_WEIGHT)
		);
	}
}
