package com.srs.tetris.bob.learn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.srs.tetris.bob.BobPlayer;
import com.srs.tetris.bob.evaluator.SapientEvaluator;
import com.srs.tetris.game.Game;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

/**
 * Runs a genetic machine learning algorithm to search for an optimal player.
 */
public class GeneticLearner {

	private static final Random random = new Random();

	private static final double INITIAL_POPULATION_VARIANCE = 2.0;

	private static final int GENERATIONS = 20;
	private static final int SPECIMENS_PER_GENERATION = 20;
	private static final double SUCCESS_THRESHOLD = 0.25;

	private static final int GAMES_PER_SPECIMEN = 8;

	private static final double MUTATE_PROBABILITY = 0.15;
	private static final double MUTATION_VARIANCE = 2.0;

	private ExecutorService executor;
	private Path outputBase;
	private int generation;
	private Gson gson;

	public GeneticLearner(Path outputBase) {
		this.outputBase = outputBase;
	}

	private void init() {
		int threads = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(threads);

		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	private void cleanUp() {
		executor.shutdown();
	}

	public List<Specimen> run() {
		init();

		List<Specimen> population = null;
		try {
			population = createInitialPopulation();
			population = runGenerations(population);

		} finally {
			cleanUp();
		}

		return population;
	}

	private List<Specimen> runGenerations(List<Specimen> population) {
		for (generation = 1; generation <= GENERATIONS; generation++) {
			System.out.printf("Generation %d\n", generation);
			population = evaluateAndReproduce(population);
		}
		return population;
	}

	private List<Specimen> evaluateAndReproduce(List<Specimen> population) {
		// First evaluate each specimen.
		AtomicInteger counter = new AtomicInteger();
		population.forEach(specimen -> {
			System.out.printf("\t%03d ", counter.incrementAndGet());
			evaluateSpecimen(specimen);
			System.out.println();
		});

		// Sort them by average lines to put the most successful first.
		Collections.sort(population, comparing(s -> s.getLines().getAverage(), reverseOrder()));

		System.out.printf("Finished generation %d\n, most successful learner avg lines: %,.2f\n\n",
			generation, population.get(0).getLines().getAverage());

		// Now write out the results for this generation.
		writeGeneration(population);

		SpecimenBreeder breeder = new SpecimenBreeder(random);
		SpecimenMutator mutator = new SpecimenMutator(random, MUTATE_PROBABILITY, MUTATION_VARIANCE);

		// Find the successful specimens for this generation (and eliminate the rest).
		List<Specimen> successfulSpecimens = population.stream()
			.limit((int) (SUCCESS_THRESHOLD * SPECIMENS_PER_GENERATION))
			.collect(toList());

		// Breed the successful ones to get children.
		List<Specimen> children = Stream.generate(() -> {
				// Pick a random mother and father.
				Specimen mother = successfulSpecimens.get(random.nextInt(successfulSpecimens.size()));
				Specimen father = successfulSpecimens.get(random.nextInt(successfulSpecimens.size()));

				// Breed them together to get the new child.
				return breeder.breed(mother, father);
			})

			// Produce random mutations in a percentage of the children.
			.map(mutator::mutate)

			// Fill up the empty spots that aren't already taken by successful specimens.
			.limit(SPECIMENS_PER_GENERATION - successfulSpecimens.size())
			.collect(toList());

		// Return the successful parents and the new children as the new population for the next generation.
		return Stream.concat(
			successfulSpecimens.stream(),
			children.stream()
		).collect(toList());
	}

	private void evaluateSpecimen(Specimen specimen) {
		if (specimen.getLines() == null) {
			PlayerEvaluator evaluator = new PlayerEvaluator(
				() -> {
					// Create a new player with the weights from this specimen.
					BobPlayer player = new BobPlayer();
					player.setBoardEvaluator(new SapientEvaluator(specimen.getWeights()));
					return player;
				},
				executor,
				GAMES_PER_SPECIMEN
			);
			evaluator.setOnGameEnd(new PrintDotConsumer<>());

			// Evaluate the specimen.
			PlayerEvaluator.Result result = evaluator.run();

			// Record the number of lines achieved.
			specimen.setLines(result.getGames().stream()
				.mapToInt(Game::getCompletedLines)
				.summaryStatistics()
			);
		}
	}

	private List<Specimen> createInitialPopulation() {
		SpecimenMutator mutator = new SpecimenMutator(random, 1.0, INITIAL_POPULATION_VARIANCE);

		return Stream.concat(
			// Include one initial specimen, unchanged.
			Stream.of(createInitialSpecimen()),

			// Create the rest of the specimens and mutate them.
			Stream.generate(this::createInitialSpecimen)
				.limit(SPECIMENS_PER_GENERATION - 1)
				.map(mutator::mutate)
		).collect(toList());
	}

	private Specimen createInitialSpecimen() {
		return new Specimen(
			UUID.randomUUID().toString(),
			new SapientEvaluator.Weights()
		);
	}

	private void writeGeneration(List<Specimen> population) {
		try {
			String json = gson.toJson(population);
			Files.write(outputBase.resolve(String.format("generation.%03d", generation)), json.getBytes());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		// Create a new directory for output files
		Path outputBase = createOutputBase();

		System.out.printf("Starting genetic learner (%s)\n\n", outputBase);

		// Create the learner and run it.
		GeneticLearner learner = new GeneticLearner(outputBase);
		learner.run();
	}

	private static Path createOutputBase() {
		String now = new DateTimeFormatterBuilder()
			.append(DateTimeFormatter.ISO_LOCAL_DATE)
			.appendLiteral('_')
			.appendValue(ChronoField.HOUR_OF_DAY, 2)
			.appendLiteral('.')
			.appendValue(ChronoField.MINUTE_OF_HOUR, 2)
			.appendLiteral('.')
			.appendValue(ChronoField.SECOND_OF_MINUTE, 2)
			.toFormatter().format(LocalDateTime.now());

		Path outputBase = Paths.get("learning-data", now);

		try {
			Files.createDirectory(outputBase);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return outputBase;
	}
}
