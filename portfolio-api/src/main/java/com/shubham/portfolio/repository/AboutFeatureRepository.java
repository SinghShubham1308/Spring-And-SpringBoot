package com.shubham.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubham.portfolio.model.AboutFeature;

/**
 * @author SinghShubham
 */
@Repository
public interface AboutFeatureRepository extends JpaRepository<AboutFeature, Long> {

}
