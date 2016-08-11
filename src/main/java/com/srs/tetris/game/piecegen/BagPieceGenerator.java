package com.srs.tetris.game.piecegen;

import com.srs.tetris.game.PieceType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static java.util.stream.Collectors.toList;

/**
 * A piece generator that guarantees an even distribution of piece types over the size of a bag.
 */
public class BagPieceGenerator implements PieceGenerator {
	private static final int DEFAULT_BAG_SIZE = PieceType.values().length;

	private Random random;
	private int bagSize;
	private Queue<PieceType> bag;

	public BagPieceGenerator() {
		this(DEFAULT_BAG_SIZE);
	}

	public BagPieceGenerator(int bagSize) {
		this(bagSize, new Random());
	}

	public BagPieceGenerator(int bagSize, long seed) {
		this(bagSize, new Random(seed));
	}

	public BagPieceGenerator(int bagSize, Random random) {
		this.bagSize = bagSize;
		this.bag = new ArrayDeque<>();
		this.random = random;
	}

	@Override
	public PieceType generate() {
		if (bag.isEmpty()) {
			fillBag();
		}
		return bag.remove();
	}

	private void fillBag() {
		List<PieceType> pieces = new ArrayList<>();

		while (pieces.size() < bagSize) {
			pieces.addAll(Arrays.stream(PieceType.values())
				.collect(toList()));
		}

		Collections.shuffle(pieces, random);

		bag.addAll(pieces.subList(0, bagSize));
	}
}
