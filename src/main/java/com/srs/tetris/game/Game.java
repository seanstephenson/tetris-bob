package com.srs.tetris.game;

import com.srs.tetris.player.Player;
import java.time.Instant;
import java.util.Random;

public class Game {

	private static Random random = new Random();

	private Player player;
	private Board board;

	private Piece piece;

	private Piece nextPiece;
	private Piece swapPiece;

	private int totalPieces;
	private int score;
	private int lines;

	private enum Status { New, InProgress, Complete }
	private Status status = Status.New;

	public Game(Player player) {
		this.player = player;
	}

	public void run() {
		if (status != Status.New) {
			throw new IllegalStateException("Cannot run a game that is not new.");
		}

		status = Status.InProgress;

		// Set up the game
		setupGame();

		while (!isGameOver()) {
			// Get the input state from the player
			Input input = player.input(this);

			// Update the game state
			updateGame();
		}

		status = Status.Complete;
	}

	private void setupGame() {
		// Create a random piece, and a random next piece.
		piece = createRandomPiece();
		nextPiece = createRandomPiece();
	}

	private void updateGame() {
		// Move the piece down.
		if (!movePieceDown()) {
			// The piece is now at the bottom.  Check for completed lines.
			checkCompleteLines();
		}
	}

	private boolean movePieceDown() {
		// Check if the current piece has blocks underneath it.
		if (!canPieceMoveDown()) {

		}
		return true;
	}

	private boolean canPieceMoveDown() {
		return false;
	}

	private void checkCompleteLines() {
		// Loop over each line and check if it is complete.
	}

	private boolean isGameOver() {
		return false;
	}

	private Piece createRandomPiece() {
		return new Piece(
			PieceType.values()[random.nextInt(PieceType.values().length)],
			Color.values()[random.nextInt(Color.values().length)]
		);
	}

	public Player getPlayer() {
		return player;
	}

	public Board getBoard() {
		return board;
	}

	public Piece getPiece() {
		return piece;
	}

	public Piece getNextPiece() {
		return nextPiece;
	}

	public Piece getSwapPiece() {
		return swapPiece;
	}
}
