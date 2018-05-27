package org.tnmk.practice.batch.partition.fileinput.joblauncher;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.tnmk.practice.batch.partition.fileinput.exception.BatchJobException;

/**
 * We already stopped batch jobs from from starting automatically by default.
 * So this configuration will help us to start the job when we want.
 */
@Configuration
public class JobLauncherConfig {

    /**
     * By default, the job execution steps will be stored in a DB. In this demo, we are using an embedded DB.
     */
    @Autowired
    private JobRepository jobRepository;

    /**
     * This configuration will prevent the batch jobs from starting automatically by default.
     * They will start only when a jobLauncher triggers their execution.
     *
     * @return
     */
    @Bean
    public JobLauncher simpleJobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        try {
            jobLauncher.afterPropertiesSet();
        } catch (Exception e) {
            throw new BatchJobException(e);
        }
        return jobLauncher;
    }
}
