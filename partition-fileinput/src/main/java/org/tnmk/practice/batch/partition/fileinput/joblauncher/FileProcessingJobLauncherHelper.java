package org.tnmk.practice.batch.partition.fileinput.joblauncher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tnmk.practice.batch.partition.fileinput.consts.JobParams;
import org.tnmk.practice.batch.partition.fileinput.exception.BatchJobException;

import java.util.UUID;

/**
 * This is just a helper class to start a job more easier.
 */
@Service
public class FileProcessingJobLauncherHelper {

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
            jobLauncher.run(job, jobParameters);
        } catch (JobRestartException | JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new BatchJobException(e);
        }
    }
}
