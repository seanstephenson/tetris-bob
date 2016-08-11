package com.srs.tetris.javafx;

import com.srs.tetris.bob.learn.FileUtil;
import com.srs.tetris.replay.Replay;
import com.srs.tetris.replay.ReplayUtil;
import com.srs.tetris.replay.Replayer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.NumberFormat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class ReplayViewer extends Application {
	private static final Color ROOT_BACKGROUND = Color.web("0x262626");

	private Replayer replayer;

	private BorderPane root;
	private BoardPane boardPane;
	private InfoTextBox lines;
	private InfoTextBox move;
	private Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		// Create the info pane.
		VBox infoPane = new VBox(15,
			lines = new InfoTextBox("Lines"),
			move = new InfoTextBox("Move")
		);
		infoPane.setPadding(new Insets(0, 0, 0, 25));

		// Create the root pane.
		root = new BorderPane();
		root.setBackground(new Background(new BackgroundFill(ROOT_BACKGROUND, null, null)));
		root.setPadding(new Insets(25));
		root.setRight(infoPane);

		// Load the latest replay file.
		loadFile(findLatestReplayFile());

		// Create the scene.
		Scene scene = new Scene(root);

		// Handle events.
		scene.setOnKeyPressed(event -> handleKeyPressed(event));
		scene.setOnDragOver(event -> handleDragOver(event));
		scene.setOnDragDropped(event -> handleDragDropped(event));

		// Configure the stage.
		primaryStage.setTitle("Replay Viewer");
		primaryStage.setScene(scene);
		primaryStage.setOnHidden((event) -> System.exit(0));
		primaryStage.show();
	}

	private void loadFile(Path path) {
		if (path == null) return;

		Replay replay = ReplayUtil.readReplay(path);
		replayer = new Replayer(replay);

		boardPane = new BoardPane(replayer.getBoard());
		root.setCenter(boardPane);

		updateUI();

		root.autosize();
		primaryStage.sizeToScene();
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

		} else if (event.getCode() == KeyCode.O) {
			loadFile(chooseReplayFile());
		}

		updateUI();
	}

	private void handleDragOver(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if (dragboard.hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY);
		} else {
			event.consume();
		}
	}

	private void handleDragDropped(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if (dragboard.hasFiles()) {
			event.setDropCompleted(true);
			loadFile(dragboard.getFiles().get(0).toPath());
		} else {
			event.setDropCompleted(false);
		}
		event.consume();
	}

	private void updateUI() {
		Platform.runLater(() -> {
			if (replayer != null) {
				// Update the info pane.
				NumberFormat formatter = NumberFormat.getIntegerInstance();
				lines.setValueText(formatter.format(replayer.getCompletedLines()));
				move.setValueText(String.format("%d of %d", replayer.getIndex(), replayer.getEnd()));

				// Update the game board.
				boardPane.update(replayer.getBoard());

				if (replayer.getPiece() != null) {
					boardPane.drawTransparentPiece(replayer.getPiece());
				}
			}
		});
	}

	private Path chooseReplayFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose Replay File");
		chooser.setInitialDirectory(FileUtil.getReplayDataBase().toFile());
		File file = chooser.showOpenDialog(primaryStage);
		return file != null ? file.toPath() : null;
	}

	private Path findLatestReplayFile() throws IOException {
		return Files.list(FileUtil.getReplayDataBase())
			.filter(Files::isRegularFile)
			.sorted(comparing(this::getLastModifiedTime, reverseOrder()))
			.findFirst()
			.orElse(null);
	}

	private FileTime getLastModifiedTime(Path path) {
		try {
			return Files.getLastModifiedTime(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
