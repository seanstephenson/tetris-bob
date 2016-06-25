package com.srs.tetris.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import static java.util.stream.Collectors.*;

/**
 * A piece generator that guarantees an even distribution of piece types over the size of a bag.
 */
public class BagPieceGenerator implements PieceGenerator {
	private static final int DEFAULT_BAG_SIZE = PieceType.values().length;

	private int bagSize;
	private Queue<Piece> bag;

	public BagPieceGenerator() {
		this(DEFAULT_BAG_SIZE);
	}

	public BagPieceGenerator(int bagSize) {
		this.bagSize = bagSize;
		this.bag = new ArrayDeque<>();
	}

	@Override
	public Piece generate() {
		if (bag.isEmpty()) {
			fillBag();
		}
		return bag.remove();
	}

	private void fillBag() {
		List<Piece> pieces = new ArrayList<>();

		while (pieces.size() < bagSize) {
			pieces.addAll(Arrays.stream(PieceType.values()).map((type) ->
				new Piece(type)
			).collect(toList()));
		}

		Collections.shuffle(pieces);

		bag.addAll(pieces.subList(0, bagSize));
	}
}
