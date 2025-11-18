package com.shubham.portfolio.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author SinghShubham
 */
@Data
@Builder
public class PersonalDto {
	private String name;
	private String title;
	private String bio;
	private String email;
	private String phone;
	private String country;
	private String state;
	private String github;
	private String linkedin;
	public String profileImage;
	public String backgroundImage;
}
