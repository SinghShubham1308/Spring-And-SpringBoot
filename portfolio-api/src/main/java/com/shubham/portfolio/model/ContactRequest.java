package com.shubham.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {
	private String name;
	private String email;
	private String message;
}
