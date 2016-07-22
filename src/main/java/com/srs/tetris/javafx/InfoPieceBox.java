package com.srs.tetris.javafx;

import javafx.scene.Node;

public class InfoPieceBox extends InfoBox {

	private PieceBox pieceBox;

	public InfoPieceBox(String labelText) {
		super(labelText, new PieceBox());

		pieceBox = (PieceBox) getContent();
	}

	public PieceBox getPieceBox() {
		return pieceBox;
	}
}
