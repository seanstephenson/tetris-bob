package com.srs.tetris.replay;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
		try (Writer out = Files.newBufferedWriter(file)) {
			writeReplay(replay, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeReplay(Replay replay, Writer writer) {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			writer.write(gson.toJson(replay));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Reads a replay from the given file.
	 */
	public static Replay readReplay(Path file) {
		try (Reader reader = Files.newBufferedReader(file)) {
			return readReplay(reader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Replay readReplay(Reader reader) {
		return new Gson().fromJson(reader, Replay.class);
	}
}
