package com.srs.tetris.replay;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.game.Piece;
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

		// Remember the settings.
		replay.setSettings(game.getSettings().clone());

		// Don't make these complex, stateful objects part of the replay.
		replay.getSettings().setPlayer(null);
		replay.getSettings().setListenerExecutor(null);
		replay.getSettings().setPieceGenerator(null);

		// Record game info,
		replay.setPlayerType(game.getSettings().getPlayer().getClass().getName());
		replay.setPieceGeneratorType(game.getSettings().getPieceGenerator().getClass().getName());

		replay.setStartTime(game.getStartTime());

		replay.setMoves(new ArrayList<>());
	}

	@Override
	public void onPieceLand(Piece piece) {
		// Record each piece as it lands.
		replay.getMoves().add(piece);
	}

	@Override
	public void onGameOver() {
		// Record the last piece that couldn't be started.
		replay.getMoves().add(game.getPiece());

		// Store final game information,
		replay.setEndTime(game.getEndTime());

		replay.setStatus(game.getStatus());
		replay.setError(game.getError());

		replay.setTotalPieces(game.getTotalPieces());
		replay.setCompletedLines(game.getCompletedLines());
		replay.setLevel(game.getLevel());
		replay.setScore(game.getScore());
	}
}
