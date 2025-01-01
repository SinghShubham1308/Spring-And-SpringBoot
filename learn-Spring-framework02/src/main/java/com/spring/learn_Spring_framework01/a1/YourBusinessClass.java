package com.spring.learn_Spring_framework01.a1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YourBusinessClass {

	private Dependency1 dependency1;

	private Dependency2 dependency2;

	public YourBusinessClass(Dependency1 dependency1, Dependency2 dependency2) {
		super();
		System.out.println("Constructor based dependency injected");
		this.dependency1 = dependency1;
		this.dependency2 = dependency2;
	}

//	@Autowired
	public void setDependency1(Dependency1 dependency1) {
		System.out.println("dependency injected setdependency1");
		this.dependency1 = dependency1;
	}

//	@Autowired
	public void setDependency2(Dependency2 dependency2) {
		System.out.println("dependency injected setdependency2");
		this.dependency2 = dependency2;
	}

	@Override
	public String toString() {
		return "YourBusinessClass [dependency1=" + dependency1 + ", dependency2=" + dependency2 + "]";
	}
}
