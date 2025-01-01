package com.spring.learn_Spring_framework01.helloworldconfiguration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

record Person(String name, int age, Address address) {

}

record Address(String firstLine, String city) {

}

@Configuration
public class HelloWorldConfiguration {

	@Bean
	public String name() {
		return "shubham";
	}

	@Bean
	public int age() {
		return 23;
	}

	@Bean
	public Person person() {
		return new Person("arjun", 24, new Address("rahim Nagar", "lucknow"));
	}

	@Bean
	@Primary
	public Address address2() {
		return new Address("jankipuram", "lucknow");
	}

	@Bean
	@Qualifier("address3Quealifier")
	public Address address3() {
		return new Address("munsipuliya", "lucknow");
	}

	@Bean
	public Person person2MethodCall() {
		return new Person(name(), age(), address2());
	}

	@Bean
	public Person person3Parameter(String name, int age, @Qualifier("address3Quealifier")Address address) {
		return new Person(name, age, address);
	}

	@Bean
	@Primary
	public Person person4Primary(String name, int age, Address address3) {
		return new Person(name, age, address3);
	}
	
	@Bean
	public Person person5Qualifier(String name, int age, Address address3) {
		return new Person(name, age, address3);
	}
}
