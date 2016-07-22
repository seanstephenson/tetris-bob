package com.srs.tetris.game.piecegen;

import com.srs.tetris.game.PieceType;

public class MonoPieceGenerator implements PieceGenerator {
	private PieceType type;

	public MonoPieceGenerator(PieceType type) {
		this.type = type;
	}

	@Override
	public PieceType generate() {
		return type;
	}
}
