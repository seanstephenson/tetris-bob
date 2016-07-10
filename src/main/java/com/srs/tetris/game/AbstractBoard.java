package com.srs.tetris.game;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.*;

public abstract class AbstractBoard<T> implements Board<T> {
	private int width;
	private int height;

	/**
	 * Constructs a new empty board with the given width and height.
	 */
	protected AbstractBoard(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	protected AbstractBoard clone() {
		try {
			return (AbstractBoard) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Converts a board value to a single character (useful for converting to and from text).
	 */
	protected abstract char valueToChar(T t);

	/**
	 * Converts a single character to a board value (useful for converting to and from text).
	 */
	protected abstract T charToValue(char c);

	/**
	 * Fills this board with values from the given text.
	 */
	protected void fillFromChars(char[][] chars) {
		assert height == chars.length;
		for (int y = 0; y < height; y++) {
			assert width == chars[y].length;
			for (int x = 0; x < width; x++) {
				T value = charToValue(chars[y][x]);
				set(x, y, value);
			}
		}
	}

	/**
	 * Converts text into a grid of characters.
	 */
	protected static char[][] tokenize(String[] lines) {
		lines = Arrays.stream(lines)
			.map(line -> line.replaceAll("\\s+", ""))
			.filter(line -> !line.isEmpty())
			.toArray(String[]::new);

		if (lines.length == 0) throw new IllegalArgumentException("Text cannot be empty");

		int width = lines[0].length();
		for (String line : lines) {
			if (line.length() != width) {
				throw new IllegalArgumentException(String.format("Expected line length %d but got %d: %s", width, line.length(), line));
			}
		}

		return Arrays.stream(lines)
			.map(String::toCharArray)
			.toArray(char[][]::new);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (x != 0) sb.append(" ");
				sb.append(valueToChar(get(x, y)));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
