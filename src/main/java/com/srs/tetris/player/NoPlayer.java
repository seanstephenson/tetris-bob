package com.srs.tetris.player;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.Input;

public class NoPlayer implements Player {
	@Override
	public Input input(Game game) {
		return new Input();
	}
}
