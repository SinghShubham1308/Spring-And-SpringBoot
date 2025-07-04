package com.microservices.vaccinationcenter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservices.vaccinationcenter.entity.VaccinationCenter;
import com.microservices.vaccinationcenter.model.Citizen;
import com.microservices.vaccinationcenter.model.RequiredResponse;
import com.microservices.vaccinationcenter.repository.CenterRepo;

/**
 * @author Shubham Singh
 */

@RestController
@RequestMapping("/vaccinationcenter")
public class VaccinationCenterController {

	private CenterRepo centerRepo;
	private RestTemplate restTemplate;

	public VaccinationCenterController(CenterRepo centerRepo, RestTemplate restTemplate) {
		this.centerRepo = centerRepo;
		this.restTemplate = restTemplate;
	}

	@PostMapping(path = "/add")
	public ResponseEntity<VaccinationCenter> addCitizen(@RequestBody VaccinationCenter vaccinationCenter) {

		VaccinationCenter vaccinationCenterAdded = centerRepo.save(vaccinationCenter);
		return new ResponseEntity<>(vaccinationCenterAdded, HttpStatus.OK);
	}

	@GetMapping(path = "/id/{id}")
	public ResponseEntity<RequiredResponse> getAllDadaBasedonCenterId(@PathVariable int id) {
		RequiredResponse requiredResponse = new RequiredResponse();
		// 1st get vaccination center detail
		Optional<VaccinationCenter> center = centerRepo.findById(id);
		if (!center.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
		requiredResponse.setCenter(center.get());

		// then get all citizen registerd to vaccination center

		try {
			ResponseEntity<List<Citizen>> response = restTemplate.exchange(
	                "http://CITIZENSERVICE/citizen/id/" + id,
	                HttpMethod.GET,
	                null,
	                new ParameterizedTypeReference<List<Citizen>>() {}
	        );
	        List<Citizen> listOfCitizens = response.getBody();
	        requiredResponse.setCitizens(listOfCitizens);
	    } catch (Exception e) {
	        // handle failure of REST call
	        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
	    }
		return new ResponseEntity<>(requiredResponse, HttpStatus.OK);
	}

}