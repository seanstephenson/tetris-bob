package com.srs.tetris.javafx;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.bob.BobSettings;
import com.srs.tetris.bob.learn.FileUtil;
import com.srs.tetris.game.GameBoard;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.game.Piece;
import com.srs.tetris.player.DirectPlayer;
import com.srs.tetris.player.LocalPlayer;
import com.srs.tetris.player.Player;
import com.srs.tetris.replay.ReplayUtil;
import java.io.IOException;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
	private List<PieceBox> nextPieces;
	private PieceBox swapPiece;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		// Create the player.
		//Player player = new LocalPlayer();
		DirectPlayer player = new BobPlayer(BobSettings.standard());

		// Create the game.
		GameSettings gameSettings = GameSettings.standard(player);
		//GameSettings gameSettings = GameSettings.direct(player);

		//gameSettings.setWidth(6);
		//gameSettings.setHeight(8);

		game = new Game(gameSettings);
		game.addListener(this);
		game.init();

		// Generate a replay.
		gameSettings.setGenerateReplay(true);

		game.addListener(new GameListener() {
			@Override
			public void onGameOver() {
				// Write out the replay to disk on game over.
				Path replayFile = FileUtil.getReplayDataBase().resolve("local-" + FileUtil.createFilenameSafeTimestamp());
				ReplayUtil.writeReplay(game.getReplay(), replayFile);
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

		// Create the next piece boxes.
		nextPieces = new ArrayList<>();
		nextPieces.add(new PieceBox("Next"));
		while (nextPieces.size() < game.getSettings().getNextPieceCount()) {
			nextPieces.add(new PieceBox(null));
		}

		BorderStroke stroke = nextPieces.get(0).getBorder().getStrokes().get(0);
		double width = stroke.getWidths().getTop();
		nextPieces.get(0).setBorder(new Border(new BorderStroke(stroke.getTopStroke(), stroke.getTopStyle(), null, new BorderWidths(width, width, 0, width))));
		for (int i = 1; i < nextPieces.size() - 1; i++) {
			nextPieces.get(i).setBorder(new Border(new BorderStroke(stroke.getTopStroke(), stroke.getTopStyle(), null, new BorderWidths(0, width, 0, width))));
		}
		nextPieces.get(nextPieces.size() - 1).setBorder(new Border(new BorderStroke(stroke.getTopStroke(), stroke.getTopStyle(), null, new BorderWidths(0, width, width, width))));

		// Create the info pane.
		VBox infoPane = new VBox(15,
			score = new InfoBox("Score"),
			level = new InfoBox("Level"),
			lines = new InfoBox("Lines"),
			new VBox(0, nextPieces.stream().toArray(PieceBox[]::new))
		);
		infoPane.setPadding(new Insets(0, 0, 0, 25));

		// Create the left pane.
		VBox leftPane = new VBox(15,
			swapPiece = new PieceBox("Hold")
		);
		leftPane.setPadding(new Insets(0, 25, 0, 0));

		// Create the root pane.
		BorderPane root = new BorderPane(boardPane);
		root.setBackground(new Background(new BackgroundFill(ROOT_BACKGROUND, null, null)));
		root.setPadding(new Insets(25));
		root.setRight(infoPane);
		root.setLeft(leftPane);

		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Configure the stage.
		primaryStage.setTitle("Tetris Bob");
		primaryStage.setScene(scene);

		primaryStage.setOnHidden((event) -> {
			// Attempt to shut down the game gracefully, then exit.
			if (!game.isGameOver()) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.submit(() -> game.cancel());

				try {
					executor.awaitTermination(1, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					// Thread was interrupted while waiting for the game to end.
					// Nothing we can really do since the app is about to exit.
				}
			}

			System.exit(0);
		});

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
	public void onPieceStart(Piece piece) {
		Platform.runLater(() -> {
			// Update the next and swap piece boxes.
			int i = 0;
			for (Piece nextPiece : game.getNextPieces()) {
				this.nextPieces.get(i++).setPieceType(nextPiece.getType());
			}

			if (game.getSwapPiece() != null) {
				swapPiece.setPieceType(game.getSwapPiece().getType());
			}
		});
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
