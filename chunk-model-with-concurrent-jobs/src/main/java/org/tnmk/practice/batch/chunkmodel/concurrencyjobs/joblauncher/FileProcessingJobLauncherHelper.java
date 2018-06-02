package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.joblauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.consts.JobParams;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.exception.BatchJobException;

import java.util.UUID;

/**
 * This is just a helper class to start a job more easier.
 */
@Service
public class FileProcessingJobLauncherHelper {
    public static final Logger logger = LoggerFactory.getLogger(FileProcessingJobLauncherHelper.class);
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job fileProcessingJob;

    public void startFileProcessJob(String inputFilePath, String outputFilePath) {
        JobParameters jobParameters = new JobParametersBuilder()
                // The job instance is determine by Job & JobParameters.
                // This param ensure that each jobInstance will have a different Id.
                .addString(JobParams.PARAM_JOB_INSTANCE_ID, UUID.randomUUID().toString(), true)
                .addString(JobParams.PARAM_INPUT_FILE_PATH, inputFilePath)
                .addString(JobParams.PARAM_OUTPUT_FILE_PATH, outputFilePath)
                .toJobParameters();
        startJob(fileProcessingJob, jobParameters);
    }

    private void startJob(Job job, JobParameters jobParameters) {
        try {
            logger.info("Starting job {} /////////////////////////////////////////////////", jobParameters);
            jobLauncher.run(job, jobParameters);
            logger.info("Finish starting job {} -----------------------------------------", jobParameters);
        } catch (JobRestartException | JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new BatchJobException(e);
        }
    }
}
