package com.shubham.portfolio.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shubham.portfolio.dto.AboutDto;
import com.shubham.portfolio.dto.AboutFeatureDto;
import com.shubham.portfolio.dto.PersonalDto;
import com.shubham.portfolio.dto.PortfolioDataDto;
import com.shubham.portfolio.dto.SkillCategoryDto;
import com.shubham.portfolio.dto.SkillDto;
import com.shubham.portfolio.exception.ResourceNotFoundException;
import com.shubham.portfolio.model.AboutFeature;
import com.shubham.portfolio.model.Skill;
import com.shubham.portfolio.model.User;
import com.shubham.portfolio.model.UserProfile;
import com.shubham.portfolio.repository.AboutFeatureRepository;
import com.shubham.portfolio.repository.SkillRepository;
import com.shubham.portfolio.repository.UserProfileRepository;
import com.shubham.portfolio.repository.UserRepository;

/**
 * @author SinghShubham
 */

@Service
public class UserProfileService {
	private final UserProfileRepository userProfileRepository;
	private final UserRepository userRepository;
	private final SkillRepository skillRepository;
	private final AboutFeatureRepository aboutFeatureRepository;

	public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository,
			SkillRepository skillRepository, AboutFeatureRepository aboutFeatureRepository) {
		super();
		this.userProfileRepository = userProfileRepository;
		this.userRepository = userRepository;
		this.skillRepository = skillRepository;
		this.aboutFeatureRepository = aboutFeatureRepository;
	}

	@Transactional(readOnly = true)
	public PortfolioDataDto getPortfolioData() {

		UserProfile profile = userProfileRepository.findDefaultProfileWithDetails().orElse(null);
		if (profile == null) {
			return null;
		}

		return mapToPortfolioDataDto(profile);
	}

	@Transactional
	public PortfolioDataDto insertUpdatePortfolioData(PortfolioDataDto dataDto) {
		User adminUser = userRepository.findByUsername("upstreamdevotion")
				.orElseThrow(() -> new ResourceNotFoundException("Admin user not found. Seeder nahi chala."));
		UserProfile profile = userProfileRepository.findByUsernameWithDetails(adminUser.getUsername())
				.orElse(new UserProfile());
		profile.setUser(adminUser);
		profile.getFeatures().clear();
		profile.getSkills().clear();

		PersonalDto personal = dataDto.getPersonal();
		profile.setName(personal.getName());
		profile.setTitle(personal.getTitle());
		profile.setBio(personal.getBio());
		profile.setEmail(personal.getEmail());
		profile.setPhone(personal.getPhone());
		profile.setCountry(personal.getCountry());
		profile.setState(personal.getState());
		profile.setGithub(personal.getGithub());
		profile.setLinkedin(personal.getLinkedin());
		profile.setProfileImage(personal.getProfileImage());
		profile.setBackgroundImage(personal.getBackgroundImage());

		AboutDto about = dataDto.getAbout();
		profile.setAboutDescription(about.getDescription());

		Set<AboutFeature> newFeatures = about.getFeatures().stream().map(dto -> AboutFeature.builder()
				.title(dto.getTitle()).description(dto.getDescription()).userProfile(profile).build())
				.collect(Collectors.toSet());
		profile.getFeatures().addAll(newFeatures);

		Set<Skill> newSkills = dataDto.getSkills().stream()
				.flatMap(categoryDto -> categoryDto.getSkills().stream()
						.map(skillDto -> Skill.builder().category(categoryDto.getCategory()).name(skillDto.getName())
								.level(skillDto.getLevel()).userProfile(profile).build()))
				.collect(Collectors.toSet());
		profile.getSkills().addAll(newSkills); // .setSkills() ki jagah
		UserProfile savedProfile = userProfileRepository.save(profile);

		return mapToPortfolioDataDto(savedProfile);
	}

	private PortfolioDataDto mapToPortfolioDataDto(UserProfile profile) {

		PersonalDto personal = PersonalDto.builder().name(profile.getName()).title(profile.getTitle())
				.bio(profile.getBio()).email(profile.getEmail()).phone(profile.getPhone()).country(profile.getCountry())
				.state(profile.getState()).github(profile.getGithub()).linkedin(profile.getLinkedin())
				.profileImage(profile.getProfileImage()).backgroundImage(profile.getBackgroundImage()).build();

		List<AboutFeatureDto> featureDtos = profile.getFeatures().stream().map(feature -> AboutFeatureDto.builder()
				.title(feature.getTitle()).description(feature.getDescription()).build()).toList();

		AboutDto aboutDto = AboutDto.builder().description(profile.getAboutDescription()).features(featureDtos).build();

		Map<String, List<SkillDto>> skillsByCategoryMap = profile.getSkills().stream()
				.collect(Collectors.groupingBy(Skill::getCategory,
						Collectors.mapping(
								skill -> SkillDto.builder().name(skill.getName()).level(skill.getLevel()).build(),
								Collectors.toList())));

		List<SkillCategoryDto> skillCategoryDtos = skillsByCategoryMap.entrySet().stream()
				.map(entry -> SkillCategoryDto.builder().category(entry.getKey()).skills(entry.getValue()).build())
				.toList();

		return PortfolioDataDto.builder().personal(personal).about(aboutDto).skills(skillCategoryDtos).build();
	}

}
