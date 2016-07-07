package com.srs.tetris.replay;

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

	private int totalPieces;
	private int completedLines;
	private int level;
	private long score;

	private Instant startTime;
	private Instant endTime;

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

	public int getTotalPieces() {
		return totalPieces;
	}

	public void setTotalPieces(int totalPieces) {
		this.totalPieces = totalPieces;
	}

	public int getCompletedLines() {
		return completedLines;
	}

	public void setCompletedLines(int completedLines) {
		this.completedLines = completedLines;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
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

	public List<Piece> getMoves() {
		return moves;
	}

	public void setMoves(List<Piece> pieces) {
		this.moves = pieces;
	}
}
