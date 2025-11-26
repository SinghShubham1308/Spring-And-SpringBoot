package com.shubham.portfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shubham.portfolio.model.UserProfile;

/**
 * @author SinghShubham
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT p FROM UserProfile p LEFT JOIN FETCH p.features LEFT JOIN FETCH p.skills WHERE p.user.username = :username")
    Optional<UserProfile> findByUsernameWithDetails(String username);
    
    @Query("SELECT p FROM UserProfile p LEFT JOIN FETCH p.features LEFT JOIN FETCH p.skills WHERE p.id = 1")
    Optional<UserProfile> findDefaultProfileWithDetails();
}
