package com.srs.tetris.bob.learn;

import com.google.gson.Gson;
import com.srs.tetris.bob.evaluator.SapientEvaluator;
import java.util.Map;

public class WeightUtil {
	private static final Gson gson = new Gson();

	/**
	 * Extract the weights as a map of double values indexed by name.
	 */
	public static Map<String, Double> extractWeights(SapientEvaluator.Weights weights) {
		// Extract them to a map, by way of JSON.
		String json = gson.toJson(weights);
		return gson.fromJson(json, Map.class);
	}

	/**
	 * Take a map of weights indexed by name and create a pojo from them.
	 */
	public static SapientEvaluator.Weights createWeights(Map<String, Double> weights) {
		// Put them into a map, by way of JSON.
		String json = gson.toJson(weights);
		return gson.fromJson(json, SapientEvaluator.Weights.class);
	}
}
