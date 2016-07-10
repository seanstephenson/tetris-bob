package com.srs.tetris;

import com.srs.tetris.bob.BobPlayer;
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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.srs.tetris.game.Color.*;

/**
 * For testing, starts a tetris game with a local UI view.
 */
public class TetrisLocal extends Application implements GameListener {

	private static final Color ROOT_BACKGROUND = Color.web("0x262626");

	private static final double BOARD_BORDER = 1.0;
	private static final Color BOARD_BORDER_COLOR = Color.web("0x202020");
	private static final Color BOARD_BACKGROUND = Color.web("0x202020");

	private static final double SQUARE_SIZE = 30.0;
	private static final double SQUARE_GAP = 1.0;

	private static final Color EMPTY_SQUARE = Color.web("0x303030");
	private static final double GHOST_SQUARE_OPACITY = 0.3;

	private Game game;

	private Scene scene;
	private Rectangle[][] boardGrid;

	private Text scoreText;
	private Text linesText;
	private Text levelText;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		// Create the player.
		Player player = new LocalPlayer();
		//DirectPlayer player = new BobPlayer();

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
		int width = game.getBoard().getWidth();
		int height = game.getBoard().getHeight();

		Pane boardPane = new Pane();
		boardPane.setBackground(new Background(new BackgroundFill(BOARD_BACKGROUND, null, null)));
		boardPane.setPrefSize(
			width * (SQUARE_SIZE + SQUARE_GAP) + SQUARE_GAP + BOARD_BORDER * 2,
			height * (SQUARE_SIZE + SQUARE_GAP) + SQUARE_GAP + BOARD_BORDER * 2
		);
		boardPane.setBorder(new Border(new BorderStroke(BOARD_BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BOARD_BORDER))));

		boardGrid = new Rectangle[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double left = x * (SQUARE_GAP + SQUARE_SIZE) + SQUARE_GAP + BOARD_BORDER;
				double top = y * (SQUARE_GAP + SQUARE_SIZE) + SQUARE_GAP + BOARD_BORDER;

				Rectangle square = new Rectangle(left, top, SQUARE_SIZE, SQUARE_SIZE);
				setSquare(square, Empty);
				boardPane.getChildren().add(square);
				boardGrid[x][y] = square;
			}
		}

		// Create the info pane.
		VBox infoPane = new VBox(15,
			createInfoBox("Score", scoreText = new Text()),
			createInfoBox("Level", levelText = new Text()),
			createInfoBox("Lines", linesText = new Text())
		);
		infoPane.setPadding(new Insets(0, 0, 0, 25));

		// Create the root pane.
		BorderPane root = new BorderPane(boardPane);
		root.setBackground(new Background(new BackgroundFill(ROOT_BACKGROUND, null, null)));
		root.setPadding(new Insets(25));
		root.setRight(infoPane);

		return root;
	}

	private VBox createInfoBox(String label, Text infoText) {
		infoText.setFont(new Font(20));
		infoText.setFill(Color.LIGHTGRAY);

		Text infoLabel = new Text(label);
		infoLabel.setFill(Color.LIGHTGRAY);

		VBox infoBox = new VBox(5,
			infoLabel,
			infoText
		);
		infoBox.setAlignment(Pos.CENTER);
		infoBox.setPrefWidth(150);
		infoBox.setPadding(new Insets(5));
		infoBox.setBackground(new Background(new BackgroundFill(EMPTY_SQUARE, null, null)));
		infoBox.setBorder(new Border(new BorderStroke(BOARD_BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BOARD_BORDER))));

		return infoBox;
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
			scoreText.setText(formatter.format(game.getScore()));
			levelText.setText(formatter.format(game.getLevel()));
			linesText.setText(formatter.format(game.getCompletedLines()));

			// Draw the board.
			GameBoard board = game.getBoard().clone();

			// Find where the piece would be if it dropped now.
			Piece droppedPiece = game.getPiece();
			while (board.canPlace(droppedPiece.moveDown())) {
				droppedPiece = droppedPiece.moveDown();
			}

			// Now put the piece on the board where it is now.
			board.place(game.getPiece());

			// Set each square color.
			for (int x = 0; x < board.getWidth(); x++) {
				for (int y = 0; y < board.getHeight(); y++) {
					setSquare(boardGrid[x][y], board.get(x, y));
				}
			}

			// Draw a semi-transparent copy of the piece in the drop location.
			for (int pieceX = 0; pieceX < droppedPiece.getBoard().getWidth(); pieceX++) {
				for (int pieceY = 0; pieceY < droppedPiece.getBoard().getHeight(); pieceY++) {
					int boardX = pieceX + droppedPiece.getX(), boardY = pieceY + droppedPiece.getY();
					if (!droppedPiece.getBoard().isEmpty(pieceX, pieceY) && board.isEmpty(boardX, boardY)) {
						setSquare(boardGrid[boardX][boardY], droppedPiece.getColor(), true);
					}
				}
			}
		});
	}

	private void setSquare(Rectangle square, com.srs.tetris.game.Color gameColor) {
		setSquare(square, gameColor, false);
	}

	private void setSquare(Rectangle square, com.srs.tetris.game.Color gameColor, boolean transparent) {
		if (gameColor != Empty) {
			Color color = translateGameColor(gameColor).deriveColor(0, 1.0, 0.85, 1.0);
			if (transparent) color = new Color(color.getRed(), color.getGreen(), color.getBlue(), GHOST_SQUARE_OPACITY);

			// Do a linear gradient with lighter color at the upper left.
			square.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
				new Stop(0.0, color.deriveColor(0, 1.0, 1.2, 1.0)), new Stop(0.4, color), new Stop(1.0, color)));

		} else {
			// Empty, just set it up here
			square.setFill(EMPTY_SQUARE);
		}
	}

	private Color translateGameColor(com.srs.tetris.game.Color gameColor) {
		switch (gameColor) {
			case Empty: return Color.TRANSPARENT;
			case Red: return Color.RED;
			case Green: return Color.GREEN;
			case Blue: return Color.BLUE;
			case Cyan: return Color.CYAN;
			case Purple: return Color.PURPLE;
			case Orange: return Color.ORANGE;
			case Yellow: return Color.YELLOW;
			default: throw new IllegalStateException();
		}
	}
}
