package com.spring.learn_Spring_framework01.game;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan
public class GamingAppLauncherApplication{
	
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				GamingAppLauncherApplication.class)) {
			annotationConfigApplicationContext.getBean(GameRunner.class).run();
			annotationConfigApplicationContext.getBean(GamingConsole.class).up();
		} catch (BeansException e) {

			e.printStackTrace();
		}
	}
}
