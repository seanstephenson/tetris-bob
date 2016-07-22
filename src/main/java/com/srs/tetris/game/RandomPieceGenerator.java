package com.srs.tetris.game;

import java.util.Random;

public class RandomPieceGenerator implements PieceGenerator {
	private Random random;

	public RandomPieceGenerator() {
		this(new Random());
	}

	public RandomPieceGenerator(long seed) {
		this(new Random(seed));
	}

	public RandomPieceGenerator(Random random) {
		this.random = random;
	}

	@Override
	public PieceType generate() {
		return PieceType.random();
	}
}
