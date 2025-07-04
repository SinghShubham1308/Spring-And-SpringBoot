package com.microservices.vaccinationcenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shubham Singh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Citizen {
	private int id;

	private String name;

	private int vaccinationCenterId;
}
