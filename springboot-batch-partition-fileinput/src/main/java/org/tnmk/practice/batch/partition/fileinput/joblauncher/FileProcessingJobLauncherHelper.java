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
import org.springframework.stereotype.Service;
import org.tnmk.practice.batch.partition.fileinput.exception.BatchJobException;

/**
 * This is just a helper class to start a job more easier.
 */
@Service
public class FileProcessingJobLauncherHelper {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job fileProcessingJob;

    public void startFileProcessJob(String filePath) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", filePath)
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
