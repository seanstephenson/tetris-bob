package com.srs.tetris.game.piecegen;

import com.srs.tetris.game.PieceType;

public class SerialPieceGenerator implements PieceGenerator{
	private int index;

	@Override
	public PieceType generate() {
		PieceType[] values = PieceType.values();
		return values[index++ % values.length];
	}
}
