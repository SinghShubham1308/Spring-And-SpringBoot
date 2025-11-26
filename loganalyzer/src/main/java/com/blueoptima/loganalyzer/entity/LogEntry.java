package com.blueoptima.loganalyzer.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "log_entries", indexes = {
		 @Index(name = "idx_enterprise_timestamp", columnList = "enterpriseId, timestamp"),
	     @Index(name = "idx_user_api", columnList = "userLogin, api"),
	     @Index(name = "idx_timestamp_ip", columnList = "timestamp, ipAddress")
 	})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	private String ipAddress;

	@Column(length = 512)
	private String userAgent;

	private Integer statusCode;

	@Column(length = 10)
	private String requestType;

	@Column(length = 255)
	private String api;

	@Column(length = 256)
	private String userLogin;

	private Long enterpriseId;

	@Column(length = 512)
	private String enterpriseName;

	private Instant timestamp;

	private Long responseTimeMs;

	private Boolean malformed;

	@Column(length = 2048)
	private String rawLine;
}