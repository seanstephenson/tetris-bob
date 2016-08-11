package com.srs.tetris.bob.learn;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class FileUtil {
	/**
	 * Gets the base location for learning data.
	 */
	public static Path getLearningDataBase() {
		return Paths.get("learning-data");
	}

	/**
	 * Gets the base location for replay data.
	 */
	public static Path getReplayDataBase() {
		return Paths.get("replay-data");
	}

	/**
	 * Creates a timestamp that is safe to use with filenames.
	 */
	public static String createFilenameSafeTimestamp() {
		return new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ISO_LOCAL_DATE)
			.appendLiteral('_')
			.appendValue(ChronoField.HOUR_OF_DAY, 2)
			.appendLiteral('.')
			.appendValue(ChronoField.MINUTE_OF_HOUR, 2)
			.appendLiteral('.')
			.appendValue(ChronoField.SECOND_OF_MINUTE, 2)
			.toFormatter().format(LocalDateTime.now());
	}
}
