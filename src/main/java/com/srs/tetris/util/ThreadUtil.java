package com.srs.tetris.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
	public static void sleep(long interval) {
		sleep(interval, () -> {});
	}

	public static void sleep(long interval, Runnable onInterrupted) {
		if (Thread.interrupted()) {
			onInterrupted.run();

		} else {
			try {
				if (interval > 0) {
					TimeUnit.MILLISECONDS.sleep(interval);
				}

			} catch (InterruptedException e) {
				onInterrupted.run();
			}
		}
	}
}
