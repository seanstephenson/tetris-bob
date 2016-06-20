package com.srs.tetris.game;

/**
 * Allows an observer to listen for events in a game.
 */
public interface GameListener {

	/**
	 * Called when the game starts.
	 */
	void onGameStart();

	/**
	 * Called when the game is over.
	 */
	void onGameOver();

	/**
	 * Called after each frame is completed.
	 */
	void onFrame();

}
