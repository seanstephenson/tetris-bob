package com.srs.tetris.javafx;

import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Board;
import com.srs.tetris.game.GameBoard;
import com.srs.tetris.game.Piece;
import com.srs.tetris.game.PieceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PieceBox extends VBox {
	private static final double PIECE_BOX_BORDER = 1.0;
	private static final Color PIECE_BOX_BORDER_COLOR = Color.web("0x202020");
	private static final Color PIECE_BOX_BACKGROUND = Color.web("0x303030");

	private Text label;
	private Pane boardContainer;

	public PieceBox(String labelText) {
		super(5);

		boardContainer = new Pane();
		boardContainer.setPrefWidth(getPrefWidth());
		boardContainer.setPrefHeight(70);

		setAlignment(Pos.CENTER);
		setPrefWidth(130);
		setPadding(new Insets(5));
		setBackground(new Background(new BackgroundFill(PIECE_BOX_BACKGROUND, null, null)));
		setBorder(new Border(new BorderStroke(PIECE_BOX_BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(PIECE_BOX_BORDER))));

		if (labelText != null) {
			label = new Text(labelText);
			label.setFill(Color.LIGHTGRAY);
			getChildren().add(label);
		}

		getChildren().add(boardContainer);

		setPieceType(null);
	}

	public void setPieceType(PieceType pieceType) {
		boardContainer.getChildren().clear();

		if (pieceType != null) {
			GameBoard board = new GameBoard(pieceType.getBoard().crop(), pieceType.getColor());
			BoardPane boardPane = new BoardPane(board);

			// Use a transparent background and border.
			boardPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
			boardPane.setBorder(null);

			boardPane.setScaleX(0.75);
			boardPane.setScaleY(0.75);

			boardContainer.getChildren().add(boardPane);

			// Center the board pane in the board container.
			boardPane.relocate((boardContainer.getWidth() - boardPane.getPrefWidth()) / 2, (boardContainer.getHeight() - boardPane.getPrefHeight()) / 2);
		}
	}
}
