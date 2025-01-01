package com.spring.learn_Spring_framework01;

import com.spring.learn_Spring_framework01.game.GameRunner;
import com.spring.learn_Spring_framework01.game.PacmanGame;

public class App01GamingBasic {
	public static void main(String[] args) {
//		MarioGame game = new MarioGame();
//		ContraGame game = new ContraGame();
		PacmanGame game = new PacmanGame();
		GameRunner gameRunner = new GameRunner(game);

		gameRunner.run();
	}
}
