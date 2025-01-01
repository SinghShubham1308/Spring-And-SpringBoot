package com.spring.learn_Spring_framework01.a1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YourBusinessClass {

	@Autowired
	private Dependency1 dependency1;
	@Autowired
	private Dependency2 dependency2;

	@Override
	public String toString() {
		return "YourBusinessClass [dependency1=" + dependency1 + ", dependency2=" + dependency2 + "]";
	}
}
