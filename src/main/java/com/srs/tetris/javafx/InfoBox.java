package com.srs.tetris.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class InfoBox extends VBox {
	private static final double INFO_BOX_BORDER = 1.0;
	private static final Color INFO_BOX_BORDER_COLOR = Color.web("0x202020");
	private static final Color INFO_BOX_BACKGROUND = Color.web("0x303030");

	private Text label;
	private Node content;

	protected InfoBox(String labelText, Node content) {
		super(5);

		this.content = content;

		label = new Text(labelText);
		label.setFill(Color.LIGHTGRAY);

		setAlignment(Pos.CENTER);
		setPrefWidth(150);
		setPadding(new Insets(5));
		setBackground(new Background(new BackgroundFill(INFO_BOX_BACKGROUND, null, null)));
		setBorder(new Border(new BorderStroke(INFO_BOX_BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(INFO_BOX_BORDER))));

		getChildren().add(label);
		getChildren().add(content);
	}

	public Node getContent() {
		return content;
	}
}
