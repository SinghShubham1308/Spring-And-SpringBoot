package com.spring.learn_Spring_framework01;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.spring.learn_Spring_framework01.game.GameRunner;

public class App03GamingSpringBeans {
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				GamingConfiguration.class)) {
			annotationConfigApplicationContext.getBean(GameRunner.class).run();;
		} catch (BeansException e) {
			
			e.printStackTrace();
		}
	}
}
