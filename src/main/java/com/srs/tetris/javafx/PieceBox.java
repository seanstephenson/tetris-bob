package com.srs.tetris.javafx;

import com.srs.tetris.game.BitBoard;
import com.srs.tetris.game.Board;
import com.srs.tetris.game.GameBoard;
import com.srs.tetris.game.Piece;
import com.srs.tetris.game.PieceType;
import java.util.Collections;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PieceBox extends VBox {
	private static final double ROTATE_TRANSITION_DURATION = 100;

	private List<PieceType> pieces;

	public PieceBox() {
		setPieces(Collections.emptyList());

		setPrefWidth(130);
	}

	/**
	 * Removes the first piece type, animating the others upward, and appending the given piecetype to the end.
	 * @param next the piece type to be appended to the end after the others are moved upward.
	 */
	public void rotate(PieceType next) {
		pieces.remove(0);
		pieces.add(next);

		// Animate each child upward.
		for (Node node : getChildren()) {
			Pane child = (Pane) node;
			TranslateTransition transition = new TranslateTransition(new Duration(ROTATE_TRANSITION_DURATION), child);
			transition.setToY(-child.getPrefHeight());

			if (child == getChildren().get(0)) {
				transition.setOnFinished(event -> {
					setPieces(pieces);
				});

			}

			transition.play();
		}
	}

	/**
	 * Sets the piece types for this container.
	 */
	public void setPieces(List<PieceType> pieces) {
		this.pieces = pieces;

		getChildren().clear();

		for (PieceType piece : pieces) {
			getChildren().add(createPiece(piece));
		}

		// Update the clip bounds.
		if (!pieces.isEmpty()) {
			double height = getChildren().stream().mapToDouble(c -> ((Pane) c).getPrefHeight()).sum();
			setClip(new Rectangle(0, 0, getWidth(), height));
		}
	}

	public List<PieceType> getPieces() {
		return pieces;
	}

	private Pane createPiece(PieceType piece) {
		// Create a container that is constant sized.
		Pane container = new Pane();
		container.setPrefWidth(getPrefWidth());
		container.setPrefHeight(70);
		container.layout();

		if (piece != null) {
			// Create a board pane with the correct piece pattern.
			GameBoard board = new GameBoard(piece.getBoard().crop(), piece.getColor());
			BoardPane boardPane = new BoardPane(board);

			// Use a transparent background and border.
			boardPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
			boardPane.setBorder(null);

			boardPane.setScaleX(0.75);
			boardPane.setScaleY(0.75);

			container.getChildren().add(boardPane);

			// Center the board in the container.
			boardPane.relocate(
				(container.getPrefWidth() - boardPane.getPrefWidth()) / 2,
				(container.getPrefHeight() - boardPane.getPrefHeight()) / 2
			);
		}

		return container;
	}
}
