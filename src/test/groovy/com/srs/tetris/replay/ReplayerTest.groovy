package com.srs.tetris.replay

import com.srs.tetris.game.GameBoard
import org.junit.Test

class ReplayerTest {
	@Test
	void replay() {
		def replay = ReplayUtil.readReplay(new InputStreamReader(getClass().getResourceAsStream("small-replay")))
		def replayer = new Replayer(replay)

		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
		""")

		assert replayer.hasNext()
		replayer.forward()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . O O
			. . . . O O
		""")

		assert replayer.hasNext()
		replayer.forward()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. S S . O O
			S S . . O O
		""")

		assert replayer.hasNext()
		replayer.forward()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . J J .
			. S S J O O
			S S . J O O
		""")

		assert replayer.hasNext()
		replayer.forward()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			T . . . . .
			T T . J J .
			S S . J O O
		""")

		assert replayer.hasNext()
		replayer.forward()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. Z Z . . .
			T . Z Z . .
			T T . J J .
			S S . J O O
		""")

		assert replayer.hasNext()
		replayer.forward()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . L .
			. . L L L .
			. Z Z . . .
			T . Z Z . .
			T T . J J .
			S S . J O O
		""")

		assert replayer.hasNext()
		replayer.forward()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. I I I I .
			. . . . L .
			. . L L L .
			. Z Z . . .
			T . Z Z . .
			T T . J J .
			S S . J O O
		""")

		assert !replayer.hasNext()
	}

	@Test
	void backward() {
		def replay = ReplayUtil.readReplay(new InputStreamReader(getClass().getResourceAsStream("small-replay")))
		def replayer = new Replayer(replay)

		replayer.end()
		assert !replayer.hasNext()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. I I I I .
			. . . . L .
			. . L L L .
			. Z Z . . .
			T . Z Z . .
			T T . J J .
			S S . J O O
		""")

		replayer.back()
		assert replayer.hasNext()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . L .
			. . L L L .
			. Z Z . . .
			T . Z Z . .
			T T . J J .
			S S . J O O
		""")

		replayer.back(2)
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			T . . . . .
			T T . J J .
			S S . J O O
		""")

		replayer.back()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . J J .
			. S S J O O
			S S . J O O
		""")
	}

	@Test
	void skip() {
		def replay = ReplayUtil.readReplay(new InputStreamReader(getClass().getResourceAsStream("small-replay")))
		def replayer = new Replayer(replay)

		replayer.forward(7)
		assert !replayer.hasNext()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. I I I I .
			. . . . L .
			. . L L L .
			. Z Z . . .
			T . Z Z . .
			T T . J J .
			S S . J O O
		""")

		replayer.start()
		assert replayer.hasNext()
		assert replayer.board == GameBoard.from("""
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
			. . . . . .
		""")
	}
}