package com.shubham.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubham.portfolio.model.Skill;

/**
 * @author SinghShubham
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>{

}
