package com.srs.tetris.game;

import com.srs.tetris.player.Player;
import java.time.Instant;
import java.util.Random;

public class Game {

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
		piece = Piece.random();
		nextPiece = Piece.random();
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
		// Try to move the piece down
		return board.canPlace(piece.moveDown());
	}

	private void checkCompleteLines() {
		// Loop over each line and check if it is complete.
	}

	private boolean isGameOver() {
		return false;
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
