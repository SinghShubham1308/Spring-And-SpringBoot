package com.blueoptima.loganalyzer.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.blueoptima.loganalyzer.entity.LogEntry;
import com.blueoptima.loganalyzer.service.LogService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LogBatchConfig {

    private static final Logger log = LoggerFactory.getLogger(LogBatchConfig.class);

    private final LogService logService;
    private final DataSource dataSource;

    /**
     * Reader: Reads lines from the input log file.
     */
    @Bean
    @StepScope
    public FlatFileItemReader<String> logReader(@Value("#{jobParameters['filePath']}") String filePath) {
        log.info("Initializing reader for file: {}", filePath);
        return new FlatFileItemReaderBuilder<String>()
                .name("logReader")
                .resource(new FileSystemResource(filePath))
                .lineMapper((line, lineNumber) -> line)
                .build();
    }

    /**
     * Processor: Converts each line into a LogEntry entity.
     */
    @Bean
    public ItemProcessor<String, LogEntry> logProcessor() {
        return line -> {
            LogEntry entry = logService.parseLine(line);
            if (entry == null) {
                log.warn("Skipping malformed line: {}", line);
            }
            return entry;
        };
    }

    /**
     * Writer: Persists processed LogEntry objects into the database.
     */
    @Bean
    public JdbcBatchItemWriter<LogEntry> logWriter() {
        return new JdbcBatchItemWriterBuilder<LogEntry>()
                .dataSource(dataSource)
                .sql("INSERT INTO log_entries (timestamp, ip_address, user_agent, request_type, api, user_login, enterprise_id, enterprise_name, status_code, response_time_ms, malformed, raw_line) " +
                        "VALUES (:timestamp, :ipAddress, :userAgent, :requestType, :api, :userLogin, :enterpriseId, :enterpriseName, :statusCode, :responseTimeMs, :malformed, :rawLine)")
                .beanMapped()
                .build();
    }

    /**
     * TaskExecutor: Provides thread pool for parallel chunk processing.
     */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("batch-job-thread-");
        executor.initialize();
        return executor;
    }

    /**
     * Step: Defines a chunk-based step to read, process, and write log data.
     */
    @Bean
    public Step logStep(JobRepository jobRepository,
                        PlatformTransactionManager txManager,
                        FlatFileItemReader<String> logReader,
                        JdbcBatchItemWriter<LogEntry> logWriter,
                        TaskExecutor taskExecutor) {
        log.info("Configuring log processing step...");
        return new StepBuilder("logStep", jobRepository)
                .<String, LogEntry>chunk(1000, txManager)
                .reader(logReader)
                .processor(logProcessor())
                .writer(logWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    /**
     * Job: Orchestrates the execution of the log processing step.
     */
    @Bean
    public Job logProcessingJob(JobRepository jobRepository, Step logStep) {
        log.info("Creating logProcessingJob...");
        return new JobBuilder("logProcessingJob", jobRepository)
                .start(logStep)
                .build();
    }

    /**
     * JobLauncher: Runs jobs asynchronously using the thread pool executor.
     */
    @Bean
    @Primary
    public JobLauncher asyncJobLauncher(JobRepository jobRepository, TaskExecutor taskExecutor) throws Exception {
        log.info("Initializing async JobLauncher with thread pool executor...");
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
