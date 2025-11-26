package com.shubham.portfolio.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author SinghShubham
 */
@Data
@Builder
public class SkillDto {
	private String name;
    private int level;
}
