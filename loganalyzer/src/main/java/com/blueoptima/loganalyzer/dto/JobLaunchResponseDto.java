package com.blueoptima.loganalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobLaunchResponseDto {
    private Long jobId;
    private String message;
}