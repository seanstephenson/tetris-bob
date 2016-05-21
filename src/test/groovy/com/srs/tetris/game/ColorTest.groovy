package com.srs.tetris.game;

import org.junit.Test

class ColorTest {
	@Test
	public void randomColor() {
		100.times {
			def color = Color.random()

			assert color != null
			assert color != Color.Empty
		}
	}
}
