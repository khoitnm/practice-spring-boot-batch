package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.joblauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.consts.JobParams;
import org.tnmk.common.batch.joblauncher.JobLauncherHelper;

/**
 * This is just a helper class to start a job more easier.
 */
@Service
public class FileProcessingJobLauncherHelper {
    public static final Logger logger = LoggerFactory.getLogger(FileProcessingJobLauncherHelper.class);
    @Autowired
    private JobLauncherHelper jobLauncherHelper;
    @Autowired
    private Job fileProcessingJob;

    public void startFileProcessJob(String inputFilePath, String outputFilePath) {
        jobLauncherHelper.startJob(fileProcessingJob,
                JobParams.PARAM_INPUT_FILE_PATH, inputFilePath,
                JobParams.PARAM_OUTPUT_FILE_PATH, outputFilePath
        );
    }
}
