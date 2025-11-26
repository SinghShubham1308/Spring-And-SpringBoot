package com.shubham.portfolio.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author SinghShubham
 */
@Data
@Builder
public class AboutFeatureDto {
    private String title;
    private String description;
}
