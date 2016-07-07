package com.srs.tetris.bob.learn

import com.srs.tetris.bob.evaluator.SapientEvaluator
import org.junit.Test

class WeightUtilTest {
	@Test
	void extract() {
		// Extract a map from the pojo
		def map = WeightUtil.extractWeights(new SapientEvaluator.Weights(
			height: -15.2,
			dangerZone: 12
		))

		assert map instanceof Map
		map.values().each { assert it instanceof Double }
		assert map['height'] == -15.2
		assert map['dangerZone'] == 12

		// Convert the map back into a pojo
		map['height'] = -13
		def weights = WeightUtil.createWeights(map);

		assert weights instanceof SapientEvaluator.Weights
		assert weights.height == -13
		assert weights.dangerZone == 12
	}
}
