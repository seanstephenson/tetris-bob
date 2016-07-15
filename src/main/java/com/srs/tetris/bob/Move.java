package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.Score;
import com.srs.tetris.game.Piece;

public class Move {
	private Piece piece;
	private Score score;

	public Move(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return piece;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
}
