package com.shubham.portfolio.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author SinghShubham
 */
@Data
@Builder
public class AboutDto {
    private String description;
    private List<AboutFeatureDto> features;
}
