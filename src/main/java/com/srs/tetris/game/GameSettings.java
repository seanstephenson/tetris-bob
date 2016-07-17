package com.srs.tetris.game;

import com.google.common.util.concurrent.MoreExecutors;
import com.srs.tetris.player.DirectPlayer;
import com.srs.tetris.player.LocalPlayer;
import com.srs.tetris.player.Player;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameSettings implements Cloneable {

	private static final Executor DEFAULT_LISTENER_EXECUTOR = Executors.newCachedThreadPool();
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
		GameSettings settings = new GameSettings(player);
		settings.setListenerExecutor(DIRECT_EXECUTOR);
		settings.setInputMode(InputMode.Direct);
		settings.setFrameInterval(0);
		settings.setPieceMoveInterval(0);
		settings.setPieceManualDownInterval(0);
		return settings;
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

	private boolean generateReplay = false;

	private Executor listenerExecutor = DEFAULT_LISTENER_EXECUTOR;

	private PieceGenerator pieceGenerator = new BagPieceGenerator();

	public enum InputMode { Normal, Direct }
	private InputMode inputMode = InputMode.Normal;

	public GameSettings(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getNextPieceCount() {
		return nextPieceCount;
	}

	public void setNextPieceCount(int nextPieceCount) {
		this.nextPieceCount = nextPieceCount;
	}

	public long getFrameInterval() {
		return frameInterval;
	}

	public void setFrameInterval(long frameInterval) {
		this.frameInterval = frameInterval;
	}

	public long getStartingDropInterval() {
		return startingDropInterval;
	}

	public void setStartingDropInterval(long startingDropInterval) {
		this.startingDropInterval = startingDropInterval;
	}

	public long getMinDropInterval() {
		return minDropInterval;
	}

	public void setMinDropInterval(long minDropInterval) {
		this.minDropInterval = minDropInterval;
	}

	public int getLinesPerLevel() {
		return linesPerLevel;
	}

	public void setLinesPerLevel(int linesPerLevel) {
		this.linesPerLevel = linesPerLevel;
	}

	public double getLevelAccelerator() {
		return levelAccelerator;
	}

	public void setLevelAccelerator(double levelAccelerator) {
		this.levelAccelerator = levelAccelerator;
	}

	public long getPieceMoveInterval() {
		return pieceMoveInterval;
	}

	public void setPieceMoveInterval(long pieceMoveInterval) {
		this.pieceMoveInterval = pieceMoveInterval;
	}

	public long getPieceManualDownInterval() {
		return pieceManualDownInterval;
	}

	public void setPieceManualDownInterval(long pieceManualDownInterval) {
		this.pieceManualDownInterval = pieceManualDownInterval;
	}

	public boolean isGenerateReplay() {
		return generateReplay;
	}

	public void setGenerateReplay(boolean generateReplay) {
		this.generateReplay = generateReplay;
	}

	public Executor getListenerExecutor() {
		return listenerExecutor;
	}

	public void setListenerExecutor(Executor listenerExecutor) {
		this.listenerExecutor = listenerExecutor;
	}

	public PieceGenerator getPieceGenerator() {
		return pieceGenerator;
	}

	public void setPieceGenerator(PieceGenerator pieceGenerator) {
		this.pieceGenerator = pieceGenerator;
	}

	public InputMode getInputMode() {
		return inputMode;
	}

	public void setInputMode(InputMode inputMode) {
		this.inputMode = inputMode;
	}
}
