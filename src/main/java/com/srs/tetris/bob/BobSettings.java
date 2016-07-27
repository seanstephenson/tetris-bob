package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.PositionEvaluator;
import com.srs.tetris.bob.evaluator.SapientEvaluator;

public class BobSettings {

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

	public BobSettings withoutSwapping() {
		return setAllowSwap(false);
	}

	public BobSettings withCurrentPieceOnly() {
		return this.withoutSwapping().setMaxDepth(1);
	}

	public BobSettings withNextPieces(int nextPieces) {
		return setMaxDepth(nextPieces + 1);
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
