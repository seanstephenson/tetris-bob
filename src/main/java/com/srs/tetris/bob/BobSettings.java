package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.PositionEvaluator;
import com.srs.tetris.bob.evaluator.SapientEvaluator;

public class BobSettings {

	/**
	 * Creates a player with the default settings.
	 */
	public static BobSettings standard() {
		return new BobSettings();
	}

	/**
	 * Creates a player that only examines the current piece (no next pieces), and doesn't swap.
	 */
	public static BobSettings simple() {
		return noSwap()
			.setMaxDepth(1);
	}

	public static BobSettings simple(PositionEvaluator positionEvaluator) {
		return noSwap(positionEvaluator)
			.setMaxDepth(1);
	}

	/**
	 * Creates a player that doesn't ever swap pieces.
	 */
	public static BobSettings noSwap() {
		return standard()
			.setAllowSwap(false);
	}

	public static BobSettings noSwap(PositionEvaluator positionEvaluator) {
		return standard()
			.setPositionEvaluator(positionEvaluator)
			.setAllowSwap(false);
	}

	private PositionEvaluator positionEvaluator;

	private boolean allowSwap = true;
	private int maxDepth = 3;

	private InputSupplier inputSupplier = new NormalInputSupplier();

	public BobSettings() {
		this(new SapientEvaluator());
	}

	public BobSettings(PositionEvaluator positionEvaluator) {
		this.positionEvaluator = positionEvaluator;
	}

	public BobSettings withRapidMovement() {
		NormalInputSupplier inputSupplier = new NormalInputSupplier();
		inputSupplier.setRapidMovement(true);
		setInputSupplier(inputSupplier);
		return this;
	}

	public BobSettings withSloppyMovement() {
		setInputSupplier(new SloppyInputSupplier());
		return this;
	}

	public boolean isAllowSwap() {
		return allowSwap;
	}

	public BobSettings setAllowSwap(boolean allowSwap) {
		this.allowSwap = allowSwap;
		return this;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public BobSettings setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
		return this;
	}

	public PositionEvaluator getPositionEvaluator() {
		return positionEvaluator;
	}

	public BobSettings setPositionEvaluator(PositionEvaluator positionEvaluator) {
		this.positionEvaluator = positionEvaluator;
		return this;
	}

	public InputSupplier getInputSupplier() {
		return inputSupplier;
	}

	public BobSettings setInputSupplier(InputSupplier inputSupplier) {
		this.inputSupplier = inputSupplier;
		return this;
	}
}
