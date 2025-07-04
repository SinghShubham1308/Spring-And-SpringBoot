package com.microservices.vaccinationcenter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shubham Singh
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationCenter {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String centerName;
	
	private String centerAddress;
}
