package com.srs.tetris;

import com.srs.tetris.game.Board;
import com.srs.tetris.game.Game;
import com.srs.tetris.game.GameListener;
import com.srs.tetris.player.NoPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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

	private Group root;
	private Rectangle[][] boardGrid;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		// Create the game.
		game = new Game(new NoPlayer());
		game.addListener(this);
		game.init();

		int width = game.getBoard().getWidth();
		int height = game.getBoard().getHeight();

		double squareSize = 30.0;

		// Create the UI objects.
		root = new Group();

		Pane boardPane = new Pane();
		boardPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		boardPane.setPrefSize(width * squareSize, height * squareSize);

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
	}

	private void setSquareColor(Rectangle square, com.srs.tetris.game.Color gameColor) {
		Color color = translateGameColor(gameColor);

		square.setFill(new LinearGradient(0, 1, 1, 0, true, CycleMethod.NO_CYCLE,
			new Stop(0, color.darker().darker()), new Stop(1, color.brighter().brighter())));

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
			case Gray: return Color.GRAY;
			default: throw new IllegalStateException();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Configure the stage.
		primaryStage.setTitle("Tetris Bob");
		primaryStage.setOnHidden((event) -> System.exit(0));
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		// Run the game in a separate thread.
		new Thread(() -> game.run()).start();
	}

	@Override
	public void onFrame() {
		Platform.runLater(() -> {
			Board board = game.getBoard().clone();
			board.place(game.getPiece());

			for (int x = 0; x < board.getWidth(); x++) {
				for (int y = 0; y < board.getHeight(); y++) {
					setSquareColor(boardGrid[x][y], board.getColor(x, y));
				}
			}
		});
	}

	@Override
	public void onGameStart() {
	}

	@Override
	public void onGameOver() {
	}
}
