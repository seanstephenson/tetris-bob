package com.srs.tetris.web;

import com.srs.tetris.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TetrisController {
	@Autowired
	private GameManager gameManager;

	@RequestMapping("/board")
	public String board() {
		return gameManager.getGame().getBoard().toString();
	}

	@RequestMapping("/game")
	public Game game() {
		return gameManager.getGame();
	}
}
