package com.srs.tetris.bob.learn;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * A consumer that prints out a dot each time it is called, and a new line after every N dots, making it suitable
 * to show progress for long operations.  This object is safe for use with multiple threads.
 */
public class PrintDotConsumer<T> implements Consumer<T> {

	private AtomicInteger counter = new AtomicInteger(0);
	private int lineLength = 100;

	@Override
	public void accept(T object) {
		System.out.print(".");
		if (counter.incrementAndGet() % lineLength == 0) System.out.println();
	}
}
