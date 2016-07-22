package com.srs.tetris.javafx;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InfoTextBox extends InfoBox {
	private Text value;

	public InfoTextBox(String labelText) {
		super(labelText, new Text());

		value = (Text) getContent();
		value.setFont(new Font(20));
		value.setFill(Color.LIGHTGRAY);
	}

	public void setValueText(String text) {
		value.setText(text);
	}
}
