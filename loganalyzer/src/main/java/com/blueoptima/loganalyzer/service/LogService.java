package com.blueoptima.loganalyzer.service;

import com.blueoptima.loganalyzer.entity.LogEntry;

public interface LogService {

	public LogEntry parseLine(String line);
}
