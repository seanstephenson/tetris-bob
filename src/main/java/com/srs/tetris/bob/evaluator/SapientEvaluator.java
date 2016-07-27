package com.srs.tetris.bob.evaluator;

/**
 * The best known combination of evaluators.
 */
public class SapientEvaluator extends CompositeEvaluator {

	public static class Weights implements Cloneable {

		/*
		private double height = -1.5;
		private double averageHeight = -1.5;

		private double dangerZone = -4.0;
		private double dangerZoneExponent = 1.25;
		private double dangerZoneSize = 10;

		private double holes = -15.0;

		private double narrowGaps = -1.5;
		private double narrowTwoGap = 1.0;
		private double narrowThreeGap = 2.0;
		private double narrowFourGap = 6.0;

		private double nearlyCompleteLines = 3.0;
		*/

		private double height = -7.401;
		private double averageHeight = -2.987;
		private double dangerZone = -9.853;
		private double dangerZoneExponent = 1.642;
		private double dangerZoneSize = 2.045;
		private double holes = -20.211;
		private double narrowGaps = -2.813;
		private double narrowTwoGap = 1.858;
		private double narrowThreeGap = 4.665;
		private double narrowFourGap = 6.000;
		private double nearlyCompleteLines = 4.500;


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

		public double getNarrowGaps() {
			return narrowGaps;
		}

		public void setNarrowGaps(double narrowGaps) {
			this.narrowGaps = narrowGaps;
		}

		public double getNarrowTwoGap() {
			return narrowTwoGap;
		}

		public void setNarrowTwoGap(double narrowTwoGap) {
			this.narrowTwoGap = narrowTwoGap;
		}

		public double getNarrowThreeGap() {
			return narrowThreeGap;
		}

		public void setNarrowThreeGap(double narrowThreeGap) {
			this.narrowThreeGap = narrowThreeGap;
		}

		public double getNarrowFourGap() {
			return narrowFourGap;
		}

		public void setNarrowFourGap(double narrowFourGap) {
			this.narrowFourGap = narrowFourGap;
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
			new WeightedEvaluator(new NarrowGapEvaluator(weights.getNarrowTwoGap(), weights.getNarrowThreeGap(), weights.getNarrowFourGap()), weights.getNarrowGaps()),
			new WeightedEvaluator(new NearlyCompletedLinesEvaluator(), weights.getNearlyCompleteLines())
		);
	}
}
