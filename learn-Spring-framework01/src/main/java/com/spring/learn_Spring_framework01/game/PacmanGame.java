package com.spring.learn_Spring_framework01.game;

public class PacmanGame implements GamingConsole {

	@Override
	public void up() {

		System.out.println("move up");
	}

	@Override
	public void down() {

		System.out.println("move down");
	}

	@Override
	public void left() {

		System.out.println("move left");
	}

	@Override
	public void right() {

		System.out.println("move right");
	}

}
