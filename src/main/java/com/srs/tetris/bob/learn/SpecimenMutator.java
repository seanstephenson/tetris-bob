package com.srs.tetris.bob.learn;

import java.util.Map;
import java.util.Random;

/**
 * Introduces random mutations to a specimen.
 */
public class SpecimenMutator {
	private Random random;
	private double mutateProbability;
	private double mutationVariance;

	public SpecimenMutator(Random random, double mutateProbability, double mutationVariance) {
		this.random = random;
		this.mutateProbability = mutateProbability;
		this.mutationVariance = mutationVariance;
	}

	public Specimen mutate(Specimen specimen) {
		// Extract weights into a map.
		Map<String, Double> weights = WeightUtil.extractWeights(specimen.getWeights());

		// Now, with a certain probabiliy mutate each weight.
		for (Map.Entry<String, Double> entry : weights.entrySet()) {
			if (random.nextDouble() <= mutateProbability) {
				entry.setValue(mutate(entry.getValue()));
			}
		}

		// Create a new specimen.
		return new Specimen(specimen.getName(), WeightUtil.createWeights(weights));
	}

	private double mutate(double value) {
		double factor = (1.0 - mutationVariance / 2.0) + (random.nextDouble() * mutationVariance);
		return factor * value;
	}
}
