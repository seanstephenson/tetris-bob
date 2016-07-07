package com.srs.tetris.replay;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import java.time.Instant;
import java.util.ArrayList;

/**
 * A game listener that records statistics about the game along with all the moves played.
 */
public class ReplayGenerator implements GameListener {

	private Game game;
	private Replay replay;

	public ReplayGenerator(Game game) {
		this.game = game;
	}

	public Replay getReplay() {
		return replay;
	}

	@Override
	public void onGameStart() {
		replay = new Replay();

		replay.setSettings(game.getSettings().clone());
		replay.getSettings().setPlayer(null);
		replay.getSettings().setListenerExecutor(null);
		replay.getSettings().setPieceGenerator(null);

		replay.setPlayerType(game.getSettings().getPlayer().getClass().getName());
		replay.setPieceGeneratorType(game.getSettings().getPieceGenerator().getClass().getName());

		replay.setStartTime(game.getStartTime());

		replay.setMoves(new ArrayList<>());
	}

	@Override
	public void onPieceLand() {
		replay.getMoves().add(game.getPiece());
	}

	@Override
	public void onGameOver() {
		replay.setEndTime(game.getEndTime());

		replay.setTotalPieces(game.getTotalPieces());
		replay.setCompletedLines(game.getCompletedLines());
		replay.setLevel(game.getLevel());
		replay.setScore(game.getScore());
	}
}
