package com.srs.tetris.replay;

import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.game.Piece;
import java.time.Instant;
import java.util.List;

/**
 * Offers the ability to replay a game loaded from disk.
 */
public class Replay {
	private GameSettings settings;

	private String playerType;
	private String pieceGeneratorType;

	private long totalPieces;
	private long completedLines;
	private long level;
	private long score;

	private Instant startTime;
	private Instant endTime;

	private Game.Status status;
	private Throwable error;

	private List<Piece> moves;

	public GameSettings getSettings() {
		return settings;
	}

	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getPieceGeneratorType() {
		return pieceGeneratorType;
	}

	public void setPieceGeneratorType(String pieceGeneratorType) {
		this.pieceGeneratorType = pieceGeneratorType;
	}

	public long getTotalPieces() {
		return totalPieces;
	}

	public void setTotalPieces(long totalPieces) {
		this.totalPieces = totalPieces;
	}

	public long getCompletedLines() {
		return completedLines;
	}

	public void setCompletedLines(long completedLines) {
		this.completedLines = completedLines;
	}

	public long getLevel() {
		return level;
	}

	public void setLevel(long level) {
		this.level = level;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public Game.Status getStatus() {
		return status;
	}

	public void setStatus(Game.Status status) {
		this.status = status;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public List<Piece> getMoves() {
		return moves;
	}

	public void setMoves(List<Piece> pieces) {
		this.moves = pieces;
	}
}
