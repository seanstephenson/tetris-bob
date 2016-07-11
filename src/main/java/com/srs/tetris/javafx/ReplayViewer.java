package com.srs.tetris.javafx;

import com.srs.tetris.bob.learn.FileUtil;
import com.srs.tetris.replay.Replay;
import com.srs.tetris.replay.ReplayUtil;
import com.srs.tetris.replay.Replayer;
import java.nio.file.Path;
import java.text.NumberFormat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReplayViewer extends Application {
	private static final Color ROOT_BACKGROUND = Color.web("0x262626");

	private Replayer replayer;

	private BoardPane boardPane;
	private InfoBox lines;
	private InfoBox move;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Path replayFile = chooseReplayFile();
		Replay replay = ReplayUtil.readReplay(replayFile);
		replayer = new Replayer(replay);

		// Create the board.
		boardPane = new BoardPane(replayer.getBoard());

		// Create the info pane.
		VBox infoPane = new VBox(15,
			lines = new InfoBox("Lines"),
			move = new InfoBox("Move")
		);
		infoPane.setPadding(new Insets(0, 0, 0, 25));

		// Update the UI.
		updateUI();

		// Create the root pane.
		BorderPane root = new BorderPane(boardPane);
		root.setBackground(new Background(new BackgroundFill(ROOT_BACKGROUND, null, null)));
		root.setPadding(new Insets(25));
		root.setRight(infoPane);

		// Create the scene.
		Scene scene = new Scene(root);

		// Handle key events.
		scene.setOnKeyPressed(event -> handleKeyPressed(event));

		// Configure the stage.
		primaryStage.setTitle("Replay Viewer");
		primaryStage.setScene(scene);
		primaryStage.setOnHidden((event) -> System.exit(0));
		primaryStage.show();
	}

	private void handleKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.LEFT && (event.isControlDown() || event.isMetaDown())) {
			replayer.start();

		} else if (event.getCode() == KeyCode.LEFT && replayer.hasPrevious()) {
			replayer.back();

		} else if (event.getCode() == KeyCode.RIGHT && (event.isControlDown() || event.isMetaDown())) {
			replayer.end();

		} else if (event.getCode() == KeyCode.RIGHT && replayer.hasNext()) {
			replayer.forward();
		}

		updateUI();
	}

	private void updateUI() {
		Platform.runLater(() -> {
			// Update the info pane.
			NumberFormat formatter = NumberFormat.getIntegerInstance();
			lines.setValueText(formatter.format(replayer.getCompletedLines()));
			move.setValueText(String.format("%d of %d", replayer.getIndex(), replayer.getEnd()));

			// Update the game board.
			boardPane.update(replayer.getBoard());
		});
	}

	private Path chooseReplayFile() {
		return FileUtil.getReplayDataBase().resolve("local-2016-07-10_01.19.19");
	}
}
