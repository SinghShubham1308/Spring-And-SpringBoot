package com.blueoptima.loganalyzer.serviceImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blueoptima.loganalyzer.entity.LogEntry;
import com.blueoptima.loganalyzer.repository.LogEntryRepository;
import com.blueoptima.loganalyzer.service.LogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for parsing log lines and managing log entries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogEntryRepository repository;

    /** Regex to match log line prefix: timestamp + log level */
    private static final Pattern LINE_PREFIX =
            Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})\\s+([A-Z]+)", Pattern.DOTALL);

    /** Regex to match key-value pairs in log lines */
    private static final Pattern KV_PAIR = Pattern.compile("([A-Za-z0-9\\-]+)=([^#]+)");

    /** Formatter for parsing log timestamps */
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    /** Map of key parsers that populate LogEntry fields */
    private static final Map<String, BiConsumer<String, LogEntry.LogEntryBuilder>> PARSERS = initializeParsers();

    /**
     * Initializes field-specific parsers to map log key-values into {@link LogEntry}.
     */
    private static Map<String, BiConsumer<String, LogEntry.LogEntryBuilder>> initializeParsers() {
        Map<String, BiConsumer<String, LogEntry.LogEntryBuilder>> map = new HashMap<>();
        map.put("IP-Address", (val, b) -> b.ipAddress(val));
        map.put("User-Agent", (val, b) -> b.userAgent(val));
        map.put("Request-Type", (val, b) -> b.requestType(val));
        map.put("API", (val, b) -> b.api(val));
        map.put("User-Login", (val, b) -> b.userLogin(val));
        map.put("EnterpriseId", (val, b) -> b.enterpriseId(Long.parseLong(val)));
        map.put("EnterpriseName", (val, b) -> b.enterpriseName(val));
        map.put("Status-Code", (val, b) -> b.statusCode(Integer.parseInt(val)));
        map.put("Response-Time", (val, b) -> b.responseTimeMs(Long.parseLong(val)));
        return Map.copyOf(map);
    }

    /**
     * Parses a single log line into a {@link LogEntry}.
     */
    @Override
    public LogEntry parseLine(String line) {
        if (line == null || line.isBlank()) {
            log.debug("Skipping empty or null log line.");
            return null;
        }

        var builder = LogEntry.builder();
        var malformed = false; // flag to track invalid/malformed log lines
        var rest = line;

        // Step 1: Parse timestamp + log level prefix
        Matcher pref = LINE_PREFIX.matcher(line);
        if (pref.find()) {
            try {
                builder.timestamp(LocalDateTime.parse(pref.group(1), TS_FORMAT).toInstant(ZoneOffset.UTC));
                rest = line.substring(pref.end()).trim();
                log.debug("Parsed timestamp successfully for line: {}", line);
            } catch (Exception ex) {
                malformed = true;
                log.warn("Failed to parse timestamp in line: {}", line, ex);
            }
        } else {
            malformed = true;
            log.warn("Line prefix mismatch, skipping timestamp parsing. Line: {}", line);
        }

        // If parsing failed, assign current timestamp as fallback
        if (malformed) {
            builder.timestamp(Instant.now());
        }

        // Step 2: Extract and process key-value pairs
        var foundKeys = new HashSet<String>();
        Matcher m = KV_PAIR.matcher(rest);
        while (m.find()) {
            String key = m.group(1).trim();
            String value = m.group(2).trim();
            foundKeys.add(key);

            var parser = PARSERS.get(key);
            if (parser != null) {
                try {
                    parser.accept(value, builder);
                    log.debug("Parsed key='{}', value='{}'", key, value);
                } catch (NumberFormatException ex) {
                    malformed = true;
                    log.warn("Number format error for key='{}', value='{}' in line: {}", key, value, line);
                }
            } else {
                log.debug("No parser defined for key='{}', skipping.", key);
            }
        }

        // Step 3: Validate required fields
        if (!foundKeys.contains("API") || !foundKeys.contains("User-Login")) {
            malformed = true;
            log.warn("Required keys missing in line: {}", line);
        }

        // If malformed, store raw line for troubleshooting
        if (malformed) {
            builder.rawLine(line);
            log.info("Stored malformed log entry. Line: {}", line);
        }

        return builder.malformed(malformed).build();
    }

    /**
     * Scheduled job that deletes logs older than 30 days.
     * Runs every day at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldLogs() {
        log.info("Executing scheduled job: deleting logs older than 30 days...");
        Instant cutoff = Instant.now().minus(30, ChronoUnit.DAYS);

        long deletedCount = repository.deleteByTimestampBefore(cutoff);
        log.info("Deleted {} log entries older than {}", deletedCount, cutoff);
    }
}
