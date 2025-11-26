package com.blueoptima.loganalyzer.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.blueoptima.loganalyzer.dto.LogActivityDto;
import com.blueoptima.loganalyzer.entity.LogEntry;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogEntryMapper {

    private final ModelMapper modelMapper;

    public LogActivityDto toDto(LogEntry logEntry) {
        if (logEntry == null) {
            return null;
        }
        return modelMapper.map(logEntry, LogActivityDto.class);
    }

    public List<LogActivityDto> toDtoList(List<LogEntry> logEntries) {
        if (logEntries == null) {
            return null;
        }
        return logEntries.stream()
                         .map(this::toDto)
                         .collect(Collectors.toList());
    }
}