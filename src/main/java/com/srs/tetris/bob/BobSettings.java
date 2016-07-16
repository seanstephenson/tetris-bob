package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.BoardEvaluator;
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

	public static BobSettings simple(BoardEvaluator boardEvaluator) {
		return noSwap()
			.setMaxDepth(1);
	}

	/**
	 * Creates a player that doesn't ever swap pieces.
	 */
	public static BobSettings noSwap() {
		return standard()
			.setAllowSwap(false);
	}

	public static BobSettings noSwap(BoardEvaluator boardEvaluator) {
		return standard()
			.setBoardEvaluator(boardEvaluator)
			.setAllowSwap(false);
	}

	private BoardEvaluator boardEvaluator;

	private boolean allowSwap = true;
	private int maxDepth = Integer.MAX_VALUE;

	public BobSettings() {
		this(new SapientEvaluator());
	}

	public BobSettings(BoardEvaluator boardEvaluator) {
		this.boardEvaluator = boardEvaluator;
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

	public BoardEvaluator getBoardEvaluator() {
		return boardEvaluator;
	}

	public BobSettings setBoardEvaluator(BoardEvaluator boardEvaluator) {
		this.boardEvaluator = boardEvaluator;
		return this;
	}
}
