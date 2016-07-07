package com.srs.tetris.bob.learn;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.srs.tetris.bob.evaluator.SapientEvaluator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Comparator.*;

public class WeightPrinter {
	public static void main(String[] args) throws IOException {
		// Load the last weights from the last generation.
		Path data = FileUtil.getLearningDataBase();

		Path lastFolder = Files.list(data)
			.sorted(comparing(Path::getFileName, reverseOrder()))
			.findFirst().orElseThrow(FileNotFoundException::new);

		Path lastGeneration = Files.list(lastFolder)
			.sorted(comparing(Path::getFileName, reverseOrder()))
			.findFirst().orElseThrow(FileNotFoundException::new);

		System.out.printf("Printing weights from: %s\n\n", lastGeneration);

		Gson gson = new Gson();

		String json = new String(Files.readAllBytes(lastGeneration));
		List<Specimen> specimens = gson.fromJson(json, new TypeToken<List<Specimen>>(){}.getType());

		Specimen best = specimens.get(0);
		Map<String, Double> weights = WeightUtil.extractWeights(best.getWeights());

		for (Field field : SapientEvaluator.Weights.class.getDeclaredFields()) {
			String name = field.getName();
			double weight = weights.get(name);
			System.out.printf("\tprivate double %s = %.3f;\n", name, weight);
		}
	}
}
