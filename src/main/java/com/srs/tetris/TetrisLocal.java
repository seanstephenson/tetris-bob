package com.srs.tetris;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.game.Board;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.game.Piece;
import com.srs.tetris.player.DirectPlayer;
import com.srs.tetris.player.LocalPlayer;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
		game = new Game(gameSettings);

		game.addListener(this);
		game.init();

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
		scoreText = new Text();
		scoreText.setFont(new Font(20));
		scoreText.setFill(Color.LIGHTGRAY);

		Text scoreLabel = new Text("Score");
		scoreLabel.setFill(Color.LIGHTGRAY);

		VBox scoreBox = new VBox(5,
			scoreLabel,
			scoreText
		);
		scoreBox.setAlignment(Pos.CENTER);
		scoreBox.setPrefWidth(150);
		scoreBox.setPadding(new Insets(5));
		scoreBox.setBackground(new Background(new BackgroundFill(EMPTY_SQUARE, null, null)));
		scoreBox.setBorder(new Border(new BorderStroke(BOARD_BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BOARD_BORDER))));

		linesText = new Text();
		linesText.setFont(new Font(20));
		linesText.setFill(Color.LIGHTGRAY);

		Text linesLabel = new Text("Lines");
		linesLabel.setFill(Color.LIGHTGRAY);

		VBox linesBox = new VBox(5,
			linesLabel,
			linesText
		);
		linesBox.setAlignment(Pos.CENTER);
		linesBox.setPrefWidth(150);
		linesBox.setPadding(new Insets(5));
		linesBox.setBackground(new Background(new BackgroundFill(EMPTY_SQUARE, null, null)));
		linesBox.setBorder(new Border(new BorderStroke(BOARD_BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BOARD_BORDER))));

		VBox infoPane = new VBox(15,
			scoreBox,
			linesBox
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
			scoreText.setText(Integer.toString(game.getScore()));
			linesText.setText(Integer.toString(game.getCompletedLines()));

			// Draw the board.
			Board board = game.getBoard().clone();

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
					setSquare(boardGrid[x][y], board.getColor(x, y));
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
