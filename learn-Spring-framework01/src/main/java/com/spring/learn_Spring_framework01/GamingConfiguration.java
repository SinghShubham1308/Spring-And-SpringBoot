package com.spring.learn_Spring_framework01;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.learn_Spring_framework01.game.GameRunner;
import com.spring.learn_Spring_framework01.game.GamingConsole;
import com.spring.learn_Spring_framework01.game.PacmanGame;

@Configuration
public class GamingConfiguration {

	@Bean
	public GamingConsole gameConsole() {
		return new PacmanGame();
	}
	
	@Bean
	public GameRunner gameRunner(GamingConsole game) {
		return new GameRunner(game);
	}
}
