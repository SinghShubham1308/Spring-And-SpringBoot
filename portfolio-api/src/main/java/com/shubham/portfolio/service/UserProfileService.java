package com.shubham.portfolio.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.shubham.portfolio.repository.UserProfileRepository;
import com.shubham.portfolio.repository.UserRepository;

/**
 * @author SinghShubham
 */

@Service
public class UserProfileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);
	private final UserProfileRepository userProfileRepository;
	private final UserRepository userRepository;

	public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
		super();
		this.userProfileRepository = userProfileRepository;
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public PortfolioDataDto getPortfolioData() {
		LOGGER.info("[UserProfileService][getPortfolioData] Inside the getPortfolioData");
		UserProfile profile = userProfileRepository.findDefaultProfileWithDetails().orElse(null);
		if (profile == null) {
			LOGGER.warn("[UserProfileService][getPortfolioData] Profile is null");
			return null;
		}

		return mapToPortfolioDataDto(profile);
	}

	@Transactional
	public PortfolioDataDto insertUpdatePortfolioData(PortfolioDataDto dataDto) {
		LOGGER.info("[UserProfileService][insertUpdatePortfolioData] Inside the insertUpdatePortfolioData {}", dataDto);
		User adminUser = userRepository.findByUsername("upstreamdevotion")
				.orElseThrow(() -> new ResourceNotFoundException("Admin user not found. Seeder nahi chala."));
		LOGGER.debug("[UserProfileService][insertUpdatePortfolioData] admin user name is {}", adminUser);
		UserProfile profile = userProfileRepository.findByUsernameWithDetails(adminUser.getUsername())
				.orElse(new UserProfile());
		LOGGER.debug("[UserProfileService][insertUpdatePortfolioData]  user profile is {}", profile);
		profile.setUser(adminUser);

		profile.setFeatures(Optional.ofNullable(profile.getFeatures()).orElseGet(HashSet::new));
		profile.setSkills(Optional.ofNullable(profile.getSkills()).orElseGet(HashSet::new));

		profile.getFeatures().clear();
		profile.getSkills().clear();
		LOGGER.debug("[UserProfileService][insertUpdatePortfolioData] setting PersonalDto from portfolioDataDto ");
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

		LOGGER.debug("[UserProfileService][insertUpdatePortfolioData] setting AboutDto  from portfolioDataDto ");
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
		LOGGER.debug("[UserProfileService][mapToPortfolioDataDto] inside portfolio data ");
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
