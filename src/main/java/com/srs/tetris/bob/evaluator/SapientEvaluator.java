package com.srs.tetris.bob.evaluator;

/**
 * The best known combination of evaluators.
 */
public class SapientEvaluator extends CompositeEvaluator {

	public static class Weights implements Cloneable {

		private double height = -1.5;
		private double averageHeight = -1.5;

		private double dangerZone = -4.0;
		private double dangerZoneExponent = 1.25;
		private double dangerZoneSize = 10;

		private double holes = -15.0;

		private double narrowGap = -1.5;

		private double nearlyCompleteLines = 3.0;

		public double getHeight() {
			return height;
		}

		public void setHeight(double height) {
			this.height = height;
		}

		public double getAverageHeight() {
			return averageHeight;
		}

		public void setAverageHeight(double averageHeight) {
			this.averageHeight = averageHeight;
		}

		public double getDangerZone() {
			return dangerZone;
		}

		public void setDangerZone(double dangerZone) {
			this.dangerZone = dangerZone;
		}

		public double getDangerZoneExponent() {
			return dangerZoneExponent;
		}

		public void setDangerZoneExponent(double dangerZoneExponent) {
			this.dangerZoneExponent = dangerZoneExponent;
		}

		public double getDangerZoneSize() {
			return dangerZoneSize;
		}

		public void setDangerZoneSize(double dangerZoneSize) {
			this.dangerZoneSize = dangerZoneSize;
		}

		public double getHoles() {
			return holes;
		}

		public void setHoles(double holes) {
			this.holes = holes;
		}

		public double getNarrowGap() {
			return narrowGap;
		}

		public void setNarrowGap(double narrowGap) {
			this.narrowGap = narrowGap;
		}

		public double getNearlyCompleteLines() {
			return nearlyCompleteLines;
		}

		public void setNearlyCompleteLines(double nearlyCompleteLines) {
			this.nearlyCompleteLines = nearlyCompleteLines;
		}

		@Override
		public Weights clone() {
			try {
				return (Weights) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new IllegalStateException();
			}
		}
	}

	public SapientEvaluator() {
		this(new Weights());
	}

	public SapientEvaluator(Weights weights) {
		super(
			new WeightedEvaluator(new HeightEvaluator(), weights.getHeight()),
			new WeightedEvaluator(new AverageHeightEvaluator(), weights.getAverageHeight()),
			new WeightedEvaluator(new DangerZoneEvaluator((int) weights.getDangerZoneSize(), weights.getDangerZoneExponent()), weights.getDangerZone()),
			new WeightedEvaluator(new HolesEvaluator(), weights.getHoles()),
			new WeightedEvaluator(new NarrowGapEvaluator(), weights.getNarrowGap()),
			new WeightedEvaluator(new NearlyCompletedLinesEvaluator(), weights.getNearlyCompleteLines())
		);
	}
}
