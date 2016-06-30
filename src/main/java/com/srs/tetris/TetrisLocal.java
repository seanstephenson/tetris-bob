package com.srs.tetris;

import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.game.Board;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.game.GameSettings;
import com.srs.tetris.game.Piece;
import com.srs.tetris.player.CompositePlayer;
import com.srs.tetris.player.LocalPlayer;
import com.srs.tetris.player.NoPlayer;
import com.srs.tetris.player.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static com.srs.tetris.game.Color.*;

/**
 * For testing, starts a tetris game with a local UI view.
 */
public class TetrisLocal extends Application implements GameListener {

	private Game game;
	private LocalPlayer localPlayer;

	private Scene scene;
	private Rectangle[][] boardGrid;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		// Create the player.
		Player player = new CompositePlayer(
			new BobPlayer(),
			localPlayer = new LocalPlayer()
		);

		// Create the game.
		GameSettings gameSettings = GameSettings.standard(player);
		game = new Game(gameSettings);

		game.addListener(this);
		game.init();

		int width = game.getBoard().getWidth();
		int height = game.getBoard().getHeight();

		double squareSize = 30.0;

		// Create the UI objects.
		Group root = new Group();

		Pane boardPane = new Pane();
		boardPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		boardPane.setPrefSize(width * squareSize, height * squareSize);
		boardPane.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));

		boardGrid = new Rectangle[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Rectangle square = new Rectangle(x * squareSize, y * squareSize, squareSize, squareSize);
				setSquareColor(square, Empty);
				boardPane.getChildren().add(square);
				boardGrid[x][y] = square;
			}
		}

		root.getChildren().add(boardPane);

		// Create the scene.
		scene = new Scene(root);
		scene.setOnKeyPressed(localPlayer);
		scene.setOnKeyReleased(localPlayer);
	}

	private void setSquareColor(Rectangle square, com.srs.tetris.game.Color gameColor) {
		setSquareColor(square, translateGameColor(gameColor));
	}

	private void setTransparentSquareColor(Rectangle square, com.srs.tetris.game.Color gameColor) {
		setSquareColor(square, makeTransparent(translateGameColor(gameColor)));
	}

	private Color makeTransparent(Color color) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.3);
	}

	private void setSquareColor(Rectangle square, Color color) {
		square.setFill(new LinearGradient(0, 1, 1, 0, true, CycleMethod.NO_CYCLE,
			new Stop(0, color.darker()), new Stop(1, color.brighter().brighter())));

		square.setStroke(color.darker().darker());
		square.setStrokeWidth(2);
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
		Platform.runLater(() -> {
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
					setSquareColor(boardGrid[x][y], board.getColor(x, y));
				}
			}

			// Draw a semi-transparent copy of the piece in the drop location.
			for (int pieceX = 0; pieceX < droppedPiece.getBoard().getWidth(); pieceX++) {
				for (int pieceY = 0; pieceY < droppedPiece.getBoard().getHeight(); pieceY++) {
					int boardX = pieceX + droppedPiece.getX(), boardY = pieceY + droppedPiece.getY();
					if (!droppedPiece.getBoard().isEmpty(pieceX, pieceY) && board.isEmpty(boardX, boardY)) {
						setTransparentSquareColor(boardGrid[boardX][boardY], droppedPiece.getColor());
					}
				}
			}
		});
	}
}
