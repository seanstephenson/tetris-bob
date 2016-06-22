package com.srs.tetris.player;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.Input;
import java.util.Arrays;
import java.util.List;

public class CompositePlayer implements Player {
	private List<Player> players;

	public CompositePlayer(Player... players) {
		this.players = Arrays.asList(players);
	}

	@Override
	public void init(Game game) {
		players.forEach((player) -> player.init(game));
	}

	@Override
	public Input input() {
		Input input = new Input();
		players.forEach((player) -> combine(input, player.input()));
		return input;
	}

	private void combine(Input input, Input other) {
		if (other.isRotateLeft()) input.setRotateLeft(true);
		if (other.isRotateRight()) input.setRotateRight(true);
		if (other.isLeft()) input.setLeft(true);
		if (other.isRight()) input.setRight(true);
		if (other.isDown()) input.setDown(true);
		if (other.isDrop()) input.setDrop(true);
		if (other.isSwap()) input.setSwap(true);
	}
}
