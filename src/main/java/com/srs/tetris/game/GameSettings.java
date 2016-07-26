package com.srs.tetris.game;

import com.google.common.util.concurrent.MoreExecutors;
import com.srs.tetris.game.piecegen.BagPieceGenerator;
import com.srs.tetris.game.piecegen.PieceGenerator;
import com.srs.tetris.player.DirectPlayer;
import com.srs.tetris.player.Player;
import java.util.concurrent.Executor;

public class GameSettings implements Cloneable {

	private static final Executor DIRECT_EXECUTOR = MoreExecutors.directExecutor();

	/**
	 * Creates a new game with standard settings.
	 */
	public static GameSettings standard(Player player) {
		return new GameSettings(player);
	}

	/**
	 * Creates a new game with no delays for instant play.
	 */
	public static GameSettings direct(DirectPlayer player) {
		return new GameSettings(player)
			.setInputMode(InputMode.Direct)
			.setFrameInterval(0)
			.setPieceMoveInterval(0)
			.setPieceManualDownInterval(0)
			.setLineCompleteDelay(0);
	}

	public GameSettings clone() {
		try {
			return (GameSettings) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	private Player player;

	private int width = 10;
	private int height = 20;

	private int nextPieceCount = 3;

	private long frameInterval = 25;

	private long startingDropInterval = 1000;
	private long minDropInterval = 100;

	private int linesPerLevel = 10;
	private double levelAccelerator = 0.25;

	private long pieceMoveInterval = 150;
	private long pieceManualDownInterval = 100;

	private long lineCompleteDelay = 300;

	private long maxLines;

	private boolean generateReplay = false;

	private Executor listenerExecutor = DIRECT_EXECUTOR;

	private PieceGenerator pieceGenerator = new BagPieceGenerator();

	public enum InputMode { Normal, Direct }
	private InputMode inputMode = InputMode.Normal;

	public GameSettings(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public GameSettings setPlayer(Player player) {
		this.player = player;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public GameSettings setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public GameSettings setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getNextPieceCount() {
		return nextPieceCount;
	}

	public GameSettings setNextPieceCount(int nextPieceCount) {
		this.nextPieceCount = nextPieceCount;
		return this;
	}

	public long getFrameInterval() {
		return frameInterval;
	}

	public GameSettings setFrameInterval(long frameInterval) {
		this.frameInterval = frameInterval;
		return this;
	}

	public long getStartingDropInterval() {
		return startingDropInterval;
	}

	public GameSettings setStartingDropInterval(long startingDropInterval) {
		this.startingDropInterval = startingDropInterval;
		return this;
	}

	public long getMinDropInterval() {
		return minDropInterval;
	}

	public GameSettings setMinDropInterval(long minDropInterval) {
		this.minDropInterval = minDropInterval;
		return this;
	}

	public int getLinesPerLevel() {
		return linesPerLevel;
	}

	public GameSettings setLinesPerLevel(int linesPerLevel) {
		this.linesPerLevel = linesPerLevel;
		return this;
	}

	public double getLevelAccelerator() {
		return levelAccelerator;
	}

	public GameSettings setLevelAccelerator(double levelAccelerator) {
		this.levelAccelerator = levelAccelerator;
		return this;
	}

	public long getPieceMoveInterval() {
		return pieceMoveInterval;
	}

	public GameSettings setPieceMoveInterval(long pieceMoveInterval) {
		this.pieceMoveInterval = pieceMoveInterval;
		return this;
	}

	public long getPieceManualDownInterval() {
		return pieceManualDownInterval;
	}

	public GameSettings setPieceManualDownInterval(long pieceManualDownInterval) {
		this.pieceManualDownInterval = pieceManualDownInterval;
		return this;
	}

	public long getLineCompleteDelay() {
		return lineCompleteDelay;
	}

	public GameSettings setLineCompleteDelay(long lineCompleteDelay) {
		this.lineCompleteDelay = lineCompleteDelay;
		return this;
	}

	public long getMaxLines() {
		return maxLines;
	}

	public GameSettings setMaxLines(long maxLines) {
		this.maxLines = maxLines;
		return this;
	}

	public boolean isGenerateReplay() {
		return generateReplay;
	}

	public GameSettings setGenerateReplay(boolean generateReplay) {
		this.generateReplay = generateReplay;
		return this;
	}

	public Executor getListenerExecutor() {
		return listenerExecutor;
	}

	public GameSettings setListenerExecutor(Executor listenerExecutor) {
		this.listenerExecutor = listenerExecutor;
		return this;
	}

	public PieceGenerator getPieceGenerator() {
		return pieceGenerator;
	}

	public GameSettings setPieceGenerator(PieceGenerator pieceGenerator) {
		this.pieceGenerator = pieceGenerator;
		return this;
	}

	public InputMode getInputMode() {
		return inputMode;
	}

	public GameSettings setInputMode(InputMode inputMode) {
		this.inputMode = inputMode;
		return this;
	}
}
