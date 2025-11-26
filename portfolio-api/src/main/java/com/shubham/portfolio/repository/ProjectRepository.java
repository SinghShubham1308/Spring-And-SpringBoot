package com.shubham.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubham.portfolio.model.Project;

/**
 * @author SinghShubham
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	List<Project> findAllByOrderByProjectTypeAsc();
}
