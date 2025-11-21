package com.shubham.portfolio.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// --- Personal Info ---
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String title;

	@Column(length = 1000)
	private String bio;

	@Column(nullable = false)
	private String email;

	private String phone;
	private String country;
	private String state;
	private String github;
	private String linkedin;
	private String profileImage;
	private String backgroundImage;

	@Column(length = 2000)
	private String aboutDescription;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
//	@Builder.Default
	private Set<AboutFeature> features = new HashSet<>();

	@OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
//	@Builder.Default
	private Set<Skill> skills = new HashSet<>();

	/*
	 * public Set<AboutFeature> getFeatures() { if (this.features == null) {
	 * this.features = new HashSet<>(); } return this.features; }
	 * 
	 * public Set<Skill> getSkills() { if (this.skills == null) { this.skills = new
	 * HashSet<>(); } return this.skills; }
	 */
}