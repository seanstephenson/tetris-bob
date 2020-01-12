package com.srs.tetris.javafx;

import com.srs.tetris.game.GameBoard;
import com.srs.tetris.game.Piece;
import java.util.List;
import javafx.animation.FillTransition;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.srs.tetris.game.Color.Empty;

/**
 * A javafx pane that displays a game board.
 */
public class BoardPane extends Pane {
	private static final double BOARD_BORDER = 1.0;
	private static final Color BOARD_BORDER_COLOR = Color.web("0x202020");
	private static final Color BOARD_BACKGROUND = Color.web("0x202020");

	private static final double SQUARE_SIZE = 30.0;
	private static final double SQUARE_GAP = 1.0;

	private static final Color EMPTY_SQUARE = Color.web("0x303030");
	private static final double GHOST_SQUARE_OPACITY = 0.3;

	private int width, height;
	private Rectangle[][] boardGrid;

	public BoardPane(int width, int height) {
		this.width = width;
		this.height = height;
		createComponents();
	}

	public BoardPane(GameBoard board) {
		this(board.getWidth(), board.getHeight());
		update(board);
	}

	private void createComponents() {
		setBackground(new Background(new BackgroundFill(BOARD_BACKGROUND, null, null)));
		setPrefSize(
			width * (SQUARE_SIZE + SQUARE_GAP) + SQUARE_GAP + BOARD_BORDER * 2,
			height * (SQUARE_SIZE + SQUARE_GAP) + SQUARE_GAP + BOARD_BORDER * 2
		);
		setBorder(new Border(new BorderStroke(BOARD_BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BOARD_BORDER))));

		boardGrid = new Rectangle[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double left = x * (SQUARE_GAP + SQUARE_SIZE) + SQUARE_GAP + BOARD_BORDER;
				double top = y * (SQUARE_GAP + SQUARE_SIZE) + SQUARE_GAP + BOARD_BORDER;

				Rectangle square = new Rectangle(left, top, SQUARE_SIZE, SQUARE_SIZE);
				getChildren().add(square);
				setSquare(square, com.srs.tetris.game.Color.Empty);
				boardGrid[x][y] = square;
			}
		}
	}

	/**
	 * Updates this pane's display to match the state of the given game board.
	 */
	public void update(GameBoard board) {
		if (width != board.getWidth() || height != board.getHeight()) throw new IllegalArgumentException();

		// Set each square color.
		for (int x = 0; x < board.getWidth(); x++) {
			for (int y = 0; y < board.getHeight(); y++) {
				setSquare(boardGrid[x][y], board.get(x, y));
			}
		}
	}

	/**
	 * Draw the given piece on the board.
	 */
	public void drawPiece(Piece piece) {
		drawPiece(piece, false);
	}

	/**
	 * Draw the given piece on the board with a transparent color.
	 */
	public void drawTransparentPiece(Piece piece) {
		drawPiece(piece, true);
	}

	/**
	 * Fades out the given lines.
	 */
	public void fadeLines(List<Integer> completed, long delay) {
		for (int line : completed) {
			for (int x = 0; x < width; x++) {
				FillTransition transition = new FillTransition(new Duration(delay), boardGrid[x][line]);
				transition.setFromValue(((LinearGradient) boardGrid[x][line].getFill()).getStops().get(0).getColor());
				transition.setToValue(EMPTY_SQUARE);
				transition.play();
			}
		}
	}

	public void drawPiece(Piece piece, boolean transparent) {
		for (int pieceX = 0; pieceX < piece.getBoard().getWidth(); pieceX++) {
			for (int pieceY = 0; pieceY < piece.getBoard().getHeight(); pieceY++) {
				int boardX = pieceX + piece.getX(), boardY = pieceY + piece.getY();
				if (!piece.getBoard().isEmpty(pieceX, pieceY)) {
					setSquare(boardGrid[boardX][boardY], piece.getColor(), transparent);
				}
			}
		}
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
			case Gray: return Color.GRAY;
			default: throw new IllegalStateException();
		}
	}
}
