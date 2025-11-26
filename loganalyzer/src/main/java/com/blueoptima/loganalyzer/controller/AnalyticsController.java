package com.blueoptima.loganalyzer.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blueoptima.loganalyzer.dto.IpCountDto;
import com.blueoptima.loganalyzer.dto.LogActivityDto;
import com.blueoptima.loganalyzer.entity.LogEntry;
import com.blueoptima.loganalyzer.mapper.LogEntryMapper;
import com.blueoptima.loganalyzer.repository.LogEntryRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsController.class);

    private final LogEntryRepository repository;
    private final LogEntryMapper logEntryMapper;

    @Operation(summary = "Get enterprise activities", 
               description = "Retrieves all activities for a given enterprise within a specified time window.")
    @GetMapping("/enterprise/{eid}/activities")
    public List<LogActivityDto> activities(
            @PathVariable("eid") Long enterpriseId,
            @Parameter(description = "Start time in ISO format", 
                       example = "2025-09-15T10:00:00")
            @RequestParam("from") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "End time in ISO format", 
                       example = "2025-09-15T18:00:00")
            @RequestParam("to") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        log.info("Fetching activities for enterpriseId: {}, from: {}, to: {}", enterpriseId, from, to);

        List<LogEntry> entries = repository.findByEnterpriseIdAndTimestampBetween(
                enterpriseId, from.toInstant(ZoneOffset.UTC), to.toInstant(ZoneOffset.UTC));

        return logEntryMapper.toDtoList(entries);
    }

    @Operation(summary = "Count unique APIs per user", 
               description = "Counts the number of distinct APIs called by a specific user.")
    @GetMapping("/user/{user}/unique-apis")
    public long uniqueApis(@PathVariable("user") String user) {
        log.info("Counting unique APIs for user: {}", user);
        return repository.countDistinctApisByUser(user);
    }

    @Operation(summary = "Top IPs for a given day", 
               description = "Fetches the top 10 most active IP addresses for a given day (yyyy-MM-dd).")
    @GetMapping("/top-ips")
    public List<IpCountDto> topIps(
            @Parameter(description = "Day in ISO format (yyyy-MM-dd)", 
                       example = "2025-09-15")
            @RequestParam("day") String day) {

        log.info("Fetching top IPs for day: {}", day);

        LocalDate localDate = LocalDate.parse(day.trim());
        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end = localDate.plusDays(1).atStartOfDay();

        return repository.topIpsForDay(start, end);
    }

    @Operation(summary = "Get failed enterprise requests", 
               description = "Retrieves failed requests (status >= 500) for a given enterprise within a time window.")
    @GetMapping("/enterprise/{eid}/failed")
    public List<LogActivityDto> failedRequests(
            @PathVariable("eid") Long enterpriseId,
            @Parameter(description = "Start time in ISO format", 
                       example = "2025-09-15T10:00:00")
            @RequestParam("from") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @Parameter(description = "End time in ISO format", 
                       example = "2025-09-15T18:00:00")
            @RequestParam("to") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        log.info("Fetching failed requests for enterpriseId: {}, from: {}, to: {}", enterpriseId, from, to);

        List<LogEntry> failedLogs = repository
                .findByStatusCodeGreaterThanEqualAndEnterpriseIdAndTimestampBetween(
                        500, enterpriseId, from.toInstant(ZoneOffset.UTC), to.toInstant(ZoneOffset.UTC));

        return logEntryMapper.toDtoList(failedLogs);
    }
}
