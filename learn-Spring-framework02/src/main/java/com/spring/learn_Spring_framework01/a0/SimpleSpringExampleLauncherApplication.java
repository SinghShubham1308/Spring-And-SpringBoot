package com.spring.learn_Spring_framework01.a0;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan
public class SimpleSpringExampleLauncherApplication{
	
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				SimpleSpringExampleLauncherApplication.class)) {
			Arrays.stream(annotationConfigApplicationContext.getBeanDefinitionNames()).forEach(System.out::println);
		} catch (BeansException e) {

			e.printStackTrace();
		}
	}
}
