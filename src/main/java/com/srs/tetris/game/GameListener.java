package com.srs.tetris.game;

/**
 * Allows an observer to listen for events in a game.
 */
public interface GameListener {

	/**
	 * Called when the game starts.
	 */
	default void onGameStart() {}

	/**
	 * Called when the game is over.
	 */
	default void onGameOver() {}

	/**
	 * Called after each frame is completed.
	 */
	default void onFrame() {}

	/**
	 * Called after each piece starts dropping.
	 */
	default void onPieceStart(Piece piece) {}

	/**
	 * Called after each piece lands.
	 */
	default void onPieceLand(Piece piece) {}

	/**
	 * Called after any lines are completed.
	 */
	default void onLinesComplete() {}
}
