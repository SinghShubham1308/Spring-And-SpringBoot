package com.blueoptima.loganalyzer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobStatusDto {
    private Long jobId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String exitDescription;
}