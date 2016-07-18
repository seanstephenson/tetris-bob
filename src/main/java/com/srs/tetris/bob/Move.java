package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.Score;
import com.srs.tetris.game.Piece;

public class Move {
	private Piece piece;
	private boolean swap;
	private Score score;

	public Move(Piece piece) {
		this.piece = piece;
	}

	public static Move swap() {
		Move move = new Move(null);
		move.swap = true;
		return move;
	}

	public Piece getPiece() {
		return piece;
	}

	public boolean isSwap() {
		return swap;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
}
