package com.microservices.vaccinationcenter.model;

import java.util.List;

import com.microservices.vaccinationcenter.entity.VaccinationCenter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Shubham Singh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequiredResponse {

	private VaccinationCenter center;
	private List<Citizen> citizens;

}