package com.shubham.portfolio.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author SinghShubham
 */
@Data
@Builder
public class PortfolioDataDto {
	private PersonalDto personal;
    private AboutDto about;
    private List<SkillCategoryDto> skills;
}
