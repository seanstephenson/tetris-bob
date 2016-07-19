package com.srs.tetris.bob.learn;

import com.srs.tetris.bob.evaluator.SapientEvaluator;
import java.util.IntSummaryStatistics;
import java.util.LongSummaryStatistics;

public class Specimen implements Cloneable {
	private String name;
	private int generation;
	private SapientEvaluator.Weights weights;

	private double averageLines;
	private LongSummaryStatistics lines;

	public Specimen(String name, int generation, SapientEvaluator.Weights weights) {
		this.name = name;
		this.generation = generation;
		this.weights = weights;
	}

	public String getName() {
		return name;
	}

	public SapientEvaluator.Weights getWeights() {
		return weights;
	}

	public int getGeneration() {
		return generation;
	}

	public double getAverageLines() {
		return averageLines;
	}

	public LongSummaryStatistics getLines() {
		return lines;
	}

	public void setLines(LongSummaryStatistics lines) {
		this.lines = lines;
		this.averageLines = lines.getAverage();
	}

	@Override
	public Specimen clone() {
		try {
			Specimen clone = (Specimen) super.clone();
			clone.weights = weights.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException();
		}
	}
}
