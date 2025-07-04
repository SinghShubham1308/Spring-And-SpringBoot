package com.microservices.citizenservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.citizenservice.entity.Citizen;

/**
 * @author Shubham Singh
 */
@Repository
public interface CitizenRepo extends JpaRepository<Citizen, Integer> {

	List<Citizen> findByVaccinationCenterId(Integer id);

}
