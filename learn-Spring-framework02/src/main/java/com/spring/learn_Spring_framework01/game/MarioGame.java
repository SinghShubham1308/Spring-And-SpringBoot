package com.spring.learn_Spring_framework01.game;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MarioGame implements GamingConsole {
	@Override
	public void up() {
		System.out.println("jump");
	}

	@Override
	public void down() {
		System.out.println("prone");
	}

	@Override
	public void left() {
		System.out.println("move backword");
	}

	@Override
	public void right() {
		System.out.println("run fast");
	}
}
