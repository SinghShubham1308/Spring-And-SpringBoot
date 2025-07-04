package com.microservices.vaccinationcenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.vaccinationcenter.entity.VaccinationCenter;

/**
 * @author Shubham Singh
 */
@Repository
public interface CenterRepo extends JpaRepository<VaccinationCenter, Integer> {

	

}
