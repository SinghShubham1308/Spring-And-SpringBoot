package com.spring.learn_Spring_framework.examples.c1;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class BusinessCalculationService {
	private  DataService dataService;
	public BusinessCalculationService(DataService dataService){
		super();
		this.dataService = dataService;
	}
	
	public int findMax() {
		return Arrays.stream(dataService.retreiveData()).max().orElse(0);
	}
}
