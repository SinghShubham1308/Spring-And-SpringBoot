package com.blueoptima.loganalyzer.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogActivityDto {

    private String ipAddress;
    private String userAgent;
    private Integer statusCode;
    private String requestType;
    private String api;
    private String userLogin;
    private Long enterpriseId;
    private String enterpriseName;
    private Instant timestamp;
    private Boolean malformed;
}