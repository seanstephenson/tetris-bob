package com.srs.tetris.bob;

import com.srs.tetris.bob.evaluator.Score;
import com.srs.tetris.game.Piece;

public class Move {
	private int x, y, orientation;
	private boolean isSwap;
	private Score score;

	public Move(Piece piece) {
		this(piece.getX(), piece.getY(), piece.getOrientation());
	}

	public Move(int x, int y, int orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getOrientation() {
		return orientation;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
}
