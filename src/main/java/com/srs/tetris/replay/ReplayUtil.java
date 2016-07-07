package com.srs.tetris.replay;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ReplayUtil {
	/**
	 * Writes the given replay data to a file.
	 */
	public static void writeReplay(Replay replay, Path file) {
		try {
			Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (instant, type, context) -> {
					return new JsonPrimitive(DateTimeFormatter.ISO_DATE_TIME.format(instant.atZone(ZoneId.systemDefault())));
				})
				.create();

			Files.write(file, gson.toJson(replay).getBytes());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
