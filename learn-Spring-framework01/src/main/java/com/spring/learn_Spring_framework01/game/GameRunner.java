package com.spring.learn_Spring_framework01.game;

import org.springframework.beans.factory.annotation.Qualifier;

public class GameRunner {
//	private MarioGame game;
//	private ContraGame game;
	private GamingConsole game;

	/*
	 * public GameRunner(MarioGame game) { super(); this.game = game; }
	 */
	
	/*
	 * public GameRunner(ContraGame game) { super(); this.game = game; }
	 */
	
	public GameRunner(GamingConsole game) {
		
		this.game = game;
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("game is running"+game);
		game.down();
		game.up();
		game.right();
		game.left();
		
	}
	
}
