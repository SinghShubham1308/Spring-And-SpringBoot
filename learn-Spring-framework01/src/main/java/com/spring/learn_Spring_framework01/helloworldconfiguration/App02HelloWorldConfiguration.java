package com.spring.learn_Spring_framework01.helloworldconfiguration;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App02HelloWorldConfiguration {

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				HelloWorldConfiguration.class)) {
			System.out.println(annotationConfigApplicationContext.getBean("name"));
			System.out.println(annotationConfigApplicationContext.getBean("age"));
			System.out.println(annotationConfigApplicationContext.getBean("person"));
			System.out.println(annotationConfigApplicationContext.getBean("address2"));
			System.out.println(annotationConfigApplicationContext.getBean(Address.class));
			System.out.println(annotationConfigApplicationContext.getBean("person2MethodCall"));
			System.out.println(annotationConfigApplicationContext.getBean("person3Parameter"));
//			Arrays.stream(annotationConfigApplicationContext.getBeanDefinitionNames()).forEach(System.out::println);
			System.out.println(annotationConfigApplicationContext.getBean("person4Primary"));
			
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}
}
