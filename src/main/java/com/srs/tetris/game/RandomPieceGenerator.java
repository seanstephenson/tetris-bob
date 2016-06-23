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
	public Piece generate() {
		return new Piece(
			PieceType.random(),
			random.nextInt(4)
		);
	}


}
