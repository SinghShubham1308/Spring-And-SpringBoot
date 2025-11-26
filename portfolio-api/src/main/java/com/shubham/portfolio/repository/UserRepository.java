package com.shubham.portfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubham.portfolio.model.User;

/**
 * @author SinghShubham
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// Spring Security ko username se user dhoondhne ke liye yeh method chahiye
	Optional<User> findByUsername(String username);
}