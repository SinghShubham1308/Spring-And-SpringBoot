package com.shubham.portfolio.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author SinghShubham
 */
@Data
@Builder
public class SkillCategoryDto {
	private String category;
    private List<SkillDto> skills;
}
