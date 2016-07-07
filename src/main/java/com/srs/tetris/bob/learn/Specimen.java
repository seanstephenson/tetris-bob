package com.srs.tetris.bob.learn;

import com.srs.tetris.bob.evaluator.SapientEvaluator;
import java.util.IntSummaryStatistics;

public class Specimen implements Cloneable {
	private String name;
	private SapientEvaluator.Weights weights;

	private IntSummaryStatistics lines;

	public Specimen(String name, SapientEvaluator.Weights weights) {
		this.name = name;
		this.weights = weights;
	}

	public String getName() {
		return name;
	}

	public SapientEvaluator.Weights getWeights() {
		return weights;
	}

	public IntSummaryStatistics getLines() {
		return lines;
	}

	public void setLines(IntSummaryStatistics lines) {
		this.lines = lines;
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
