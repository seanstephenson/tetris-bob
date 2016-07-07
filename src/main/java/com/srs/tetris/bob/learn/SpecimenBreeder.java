package com.srs.tetris.bob.learn;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Breeds two specimens together, resulting in a brand new child specimen that has attributes of each.
 */
public class SpecimenBreeder {

	// The probability that the weights will be averaged together rather than copied at random from mother or father.
	private static final double COMBINE_PROBABILITY = 0.2;

	private Random random;
	private int generation;

	public SpecimenBreeder(Random random, int generation) {
		this.random = random;
		this.generation = generation;
	}

	/**
	 * Combines the parents and returns the new child.
	 */
	public Specimen breed(Specimen mother, Specimen father) {
		Map<String, Double> motherWeights = WeightUtil.extractWeights(mother.getWeights());
		Map<String, Double> fatherWeights = WeightUtil.extractWeights(father.getWeights());

		Map<String, Double> weights = new HashMap<>();

		for (String key : motherWeights.keySet()) {
			if (random.nextDouble() <= COMBINE_PROBABILITY) {
				// Combine the weights by averaging them together.
				double average = motherWeights.get(key) + fatherWeights.get(key) / 2.0;
				weights.put(key, average);

			} else {
				// Choose either the mother weight or the father weight at random.
				if (random.nextDouble() < 0.5) {
					weights.put(key, motherWeights.get(key));
				} else {
					weights.put(key, fatherWeights.get(key));
				}
			}
		}

		return new Specimen(
			UUID.randomUUID().toString(),
			generation,
			WeightUtil.createWeights(weights)
		);
	}
}
