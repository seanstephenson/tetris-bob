package com.srs.tetris.game;

import java.util.Random;

import static java.awt.Color.*;

public enum Color {
	Empty(0, 0, 0),
	Red(255, 0, 0),
	Green(0, 255, 0),
	Blue(0, 0, 255),
	Cyan(0, 255, 255),
	Purple(160, 32, 240),
	Orange(255, 125, 0),
	Yellow(255, 255, 0),
	Gray(128, 128, 128);

	private static Random random = new Random();

	private int r, g, b;

	Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getRed() {
		return r;
	}

	public int getGreen() {
		return g;
	}

	public int getBlue() {
		return b;
	}

	public static Color random() {
		// Pick a random color that isn't empty.
		return values()[random.nextInt(values().length - 1) + 1];
	}
}
