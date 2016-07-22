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
				def piece = generator.generate()
				assert pieceTypes.contains(piece.type) : "Same piece type was generated twice before all others were used: $piece.type"

				pieceTypes.remove(piece.type)
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
				def piece = generator.generate()
				assert pieceTypes.contains(piece.type) : "Same piece type was generated twice before all others were used: $piece.type"

				pieceTypes.remove(piece.type)
			}
		}
	}
}
