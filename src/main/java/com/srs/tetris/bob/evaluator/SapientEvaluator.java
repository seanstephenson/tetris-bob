package com.srs.tetris.bob.evaluator;

/**
 * The best known combination of evaluators.
 */
public class SapientEvaluator extends CompositeEvaluator {

	public static class Weights {
		public double height = -1.5;
		public double averageHeight = -1.5;

		public double dangerZone = -4.0;
		public double dangerZoneExponent = 1.25;
		public double dangerZoneSize = 10;

		public double holes = -15.0;

		public double narrowGap = -1.5;

		public double nearlyCompleteLines = 3.0;
	}

	public SapientEvaluator() {
		this(new Weights());
	}

	public SapientEvaluator(Weights weights) {
		super(
			new WeightedEvaluator(new HeightEvaluator(), weights.height),
			new WeightedEvaluator(new AverageHeightEvaluator(), weights.averageHeight),
			new WeightedEvaluator(new DangerZoneEvaluator((int) weights.dangerZoneSize, weights.dangerZoneExponent), weights.dangerZone),
			new WeightedEvaluator(new HolesEvaluator(), weights.holes),
			new WeightedEvaluator(new NarrowGapEvaluator(), weights.narrowGap),
			new WeightedEvaluator(new NearlyCompletedLinesEvaluator(), weights.nearlyCompleteLines)
		);
	}
}
