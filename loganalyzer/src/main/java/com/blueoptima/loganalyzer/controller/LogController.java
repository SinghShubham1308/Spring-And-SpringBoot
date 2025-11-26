package com.blueoptima.loganalyzer.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blueoptima.loganalyzer.dto.JobLaunchResponseDto;
import com.blueoptima.loganalyzer.dto.JobStatusDto;

import lombok.RequiredArgsConstructor;

/** Controller to trigger the log processing batch job and check its status. */
@RestController
@RequiredArgsConstructor
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    private final JobLauncher jobLauncher;
    private final Job logProcessingJob;
    private final JobExplorer jobExplorer; 

    @PostMapping("/upload")
    public ResponseEntity<JobLaunchResponseDto> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty() || !isAllowedFile(file.getOriginalFilename())) {
            throw new IllegalArgumentException("Invalid or empty file. Only .log and .txt files are allowed.");
        }

        Path tempFile = Files.createTempFile("upload-", "-" + file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        log.info("Launching job for uploaded file: {}", tempFile.toAbsolutePath());

        JobParameters params = new JobParametersBuilder()
                .addString("filePath", tempFile.toAbsolutePath().toString())
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        // Capture the JobExecution to get its ID
        JobExecution jobExecution = jobLauncher.run(logProcessingJob, params);
        Long jobId = jobExecution.getId();

        JobLaunchResponseDto response = new JobLaunchResponseDto(jobId,
                "Job successfully submitted for file: " + file.getOriginalFilename());

        // Return 202 Accepted with the job ID in the body
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("/read-from-path")
    public ResponseEntity<JobLaunchResponseDto> readFromPath(@RequestParam("path") String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || !isAllowedFile(file.getName())) {
            throw new IllegalArgumentException("File does not exist or has an invalid type.");
        }

        log.info("Launching job for file path: {}", filePath);

        JobParameters params = new JobParametersBuilder()
                .addString("filePath", file.getAbsolutePath())
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        
        log.info("Starting job launch at: {}", System.currentTimeMillis());
        long startTime = System.currentTimeMillis();
        JobExecution jobExecution = jobLauncher.run(logProcessingJob, params);
        long endTime = System.currentTimeMillis();
        log.info("Job launch completed in {} ms", (endTime - startTime));
        Long jobId = jobExecution.getId();

        JobLaunchResponseDto response = new JobLaunchResponseDto(jobId,
                "Job successfully submitted for the file at path: " + filePath);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    /**
     * Endpoint to check the status of a submitted job.
     */
    @GetMapping("/job-status/{jobId}")
    public ResponseEntity<JobStatusDto> getJobStatus(@PathVariable Long jobId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(jobId);

        if (jobExecution == null) {
            log.warn("No job found with ID: {}", jobId);
            return ResponseEntity.notFound().build();
        }

        JobStatusDto statusDto = new JobStatusDto(
            jobId,
            jobExecution.getStatus().toString(),
            jobExecution.getStartTime(),
            jobExecution.getEndTime(),
            jobExecution.getExitStatus().getExitDescription()
        );

        return ResponseEntity.ok(statusDto);
    }

    private boolean isAllowedFile(String filename) {
        return filename != null &&
               (filename.toLowerCase().endsWith(".log") || filename.toLowerCase().endsWith(".txt"));
    }
}