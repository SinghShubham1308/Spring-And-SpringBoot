package com.spring.learn_Spring_framework01.a1;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@Configuration
@ComponentScan
public class DepInjectionLauncherApplication{
	
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				DepInjectionLauncherApplication.class)) {
			Arrays.stream(annotationConfigApplicationContext.getBeanDefinitionNames()).forEach(System.out::println);
			System.out.println(annotationConfigApplicationContext.getBean(YourBusinessClass.class));
		} catch (BeansException e) {

			e.printStackTrace();
		}
	}
}
