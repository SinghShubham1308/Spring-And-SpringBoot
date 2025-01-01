package com.spring.learn_Spring_framework.examples.c1;

import org.springframework.stereotype.Component;

@Component
public class MySqlDbDataService implements DataService {

	@Override
	public int[] retreiveData() {
		// TODO Auto-generated method stub
		return new int[] { 1, 2, 3, 4, 5 };
	}

}
