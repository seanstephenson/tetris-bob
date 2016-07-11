package com.srs.tetris.replay;

import com.srs.tetris.game.Board;
import com.srs.tetris.game.GameBoard;

public class Replayer {
	private Replay replay;
	private GameBoard board;
	private int index;

	private int completedLines;

	public Replayer(Replay replay) {
		this.replay = replay;
		start();
	}

	public int getIndex() {
		return index;
	}

	public int getEnd() {
		return replay.getMoves().size();
	}

	public boolean hasPrevious() {
		return getIndex() > 0;
	}

	public boolean hasNext() {
		return getIndex() < getEnd();
	}

	public void forward() {
		if (!hasNext()) throw new IllegalStateException();

		board.place(replay.getMoves().get(index++));
		completedLines += board.removeCompleteLines();
	}

	public void forward(int count) {
		while (--count >= 0) {
			forward();
		}
	}

	public void back() {
		back(1);
	}

	public void back(int count) {
		if (count < 0) throw new IllegalArgumentException();

		int target = getIndex() - count;
		if (count < 0) throw new IllegalArgumentException();

		start();
		forward(target);
	}

	public void start() {
		// Create the initial game board.
		board = new GameBoard(replay.getSettings().getWidth(), replay.getSettings().getHeight());

		// Start at the beginning.
		index = 0;
		completedLines = 0;
	}

	public void end() {
		while (hasNext()) {
			forward();
		}
	}

	public GameBoard getBoard() {
		return board;
	}

	public int getCompletedLines() {
		return completedLines;
	}
}
