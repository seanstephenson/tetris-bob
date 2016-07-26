package com.srs.tetris.bob.learn;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;

class PrintCompleteLinesListener implements GameListener {
	private final Game game;

	public PrintCompleteLinesListener(Game game) {
		this.game = game;
	}

	@Override
	public void onLinesComplete() {
		if (game.getCompletedLines() % 1000 == 0) {
			System.out.println(game.getCompletedLines());
		}
	}
}
