package com.srs.tetris.javafx;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.bob.learn.FileUtil;
import com.srs.tetris.game.GameBoard;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.game.Piece;
import com.srs.tetris.player.DirectPlayer;
import com.srs.tetris.player.LocalPlayer;
import com.srs.tetris.replay.ReplayUtil;
import java.io.IOException;
import java.text.NumberFormat;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Starts and views a Tetris game with a local javafx UI.
 */
public class TetrisFX extends Application implements GameListener {

	private static final Color ROOT_BACKGROUND = Color.web("0x262626");

	private Game game;

	private Scene scene;
	private BoardPane boardPane;

	private InfoBox score;
	private InfoBox lines;
	private InfoBox level;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		// Create the player.
		//Player player = new LocalPlayer();
		DirectPlayer player = new BobPlayer();

		// Create the game.
		GameSettings gameSettings = GameSettings.standard(player);
		//GameSettings gameSettings = GameSettings.direct(player);

		//gameSettings.setWidth(6);
		//gameSettings.setHeight(8);
		gameSettings.setGenerateReplay(true);

		game = new Game(gameSettings);
		game.addListener(this);
		game.init();

		// Write out the replay to disk on game over.
		game.addListener(new GameListener() {
			@Override
			public void onGameOver() {
				ReplayUtil.writeReplay(game.getReplay(), FileUtil.getReplayDataBase().resolve("local-" + FileUtil.createFilenameSafeTimestamp()));
			}
		});

		// Create the scene.
		scene = new Scene(createUI());

		if (player instanceof LocalPlayer) {
			LocalPlayer localPlayer = (LocalPlayer) player;
			scene.setOnKeyPressed(localPlayer);
			scene.setOnKeyReleased(localPlayer);
		}
	}

	private Pane createUI() throws IOException {
		// Create the board.
		boardPane = new BoardPane(game.getBoard());

		// Create the info pane.
		VBox infoPane = new VBox(15,
			score = new InfoBox("Score"),
			level = new InfoBox("Level"),
			lines = new InfoBox("Lines")
		);
		infoPane.setPadding(new Insets(0, 0, 0, 25));

		// Create the root pane.
		BorderPane root = new BorderPane(boardPane);
		root.setBackground(new Background(new BackgroundFill(ROOT_BACKGROUND, null, null)));
		root.setPadding(new Insets(25));
		root.setRight(infoPane);

		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Configure the stage.
		primaryStage.setTitle("Tetris Bob");
		primaryStage.setScene(scene);
		primaryStage.setOnHidden((event) -> System.exit(0));
		primaryStage.show();

		// Run the game in a separate thread.
		new Thread(() -> game.run()).start();
	}

	@Override
	public void onFrame() {
		if (game.getSettings().getInputMode() != GameSettings.InputMode.Direct) {
			updateUI();
		}
	}

	@Override
	public void onGameOver() {
		updateUI();
	}

	private void updateUI() {
		Platform.runLater(() -> {
			// Update the info pane.
			NumberFormat formatter = NumberFormat.getIntegerInstance();
			score.setValueText(formatter.format(game.getScore()));
			level.setValueText(formatter.format(game.getLevel()));
			lines.setValueText(formatter.format(game.getCompletedLines()));

			// Update the game board.
			GameBoard board = game.getBoard();
			boardPane.update(board);

			// Find where the current piece would be if it dropped now.
			Piece droppedPiece = game.getPiece();
			while (board.canPlace(droppedPiece.moveDown())) {
				droppedPiece = droppedPiece.moveDown();
			}

			// Draw a semi-transparent copy of the piece in the drop location.
			boardPane.drawTransparentPiece(droppedPiece);

			// Draw the current piece last (overwriting everything else).
			boardPane.drawPiece(game.getPiece());
		});
	}
}
