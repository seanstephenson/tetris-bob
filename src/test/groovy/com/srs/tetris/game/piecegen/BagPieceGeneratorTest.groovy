package com.srs.tetris.game.piecegen

import com.srs.tetris.game.PieceType
import org.junit.Test

class BagPieceGeneratorTest {
	@Test
	void bag() {
		def generator = new BagPieceGenerator()

		// Make sure each piece comes up at least once before any others
		100.times {
			def pieceTypes = new ArrayList<>(Arrays.asList(PieceType.values()))
			while (!pieceTypes.isEmpty()) {
				def pieceType = generator.generate()
				assert pieceTypes.contains(pieceType) : "Same piece type was generated twice before all others were used: $pieceType.type"

				pieceTypes.remove(pieceType)
			}
		}
	}

	@Test
	void largeBag() {
		def generator = new BagPieceGenerator(PieceType.values().length * 4)

		// Make sure each piece comes up 4 times before others come up more
		100.times {
			def pieceTypes = []
			4.times { pieceTypes.addAll(Arrays.asList(PieceType.values())) }

			while (!pieceTypes.isEmpty()) {
				def pieceType = generator.generate()
				assert pieceTypes.contains(pieceType) : "Same piece type was generated too many times before all others were used: $pieceType.type"

				pieceTypes.remove(pieceType)
			}
		}
	}
}
