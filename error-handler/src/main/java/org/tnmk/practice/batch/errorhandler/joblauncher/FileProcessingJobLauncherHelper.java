package org.tnmk.practice.batch.errorhandler.joblauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tnmk.common.batch.joblauncher.JobLauncherHelper;
import org.tnmk.practice.batch.errorhandler.consts.JobParams;

/**
 * This is just a helper class to start a job more easier.
 */
@Service
public class FileProcessingJobLauncherHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingJobLauncherHelper.class);
    @Autowired
    private JobLauncherHelper jobLauncherHelper;
    @Autowired
    private Job fileProcessingJob;

    public void startFileProcessJob(String inputFilePath, int chunkSize, int threadsCount) {
        try {
            jobLauncherHelper.startJob(fileProcessingJob,
                    JobParams.PARAM_INPUT_FILE_PATH, inputFilePath,
                    JobParams.PARAM_CHUNK_SIZE, chunkSize,
                    JobParams.PARAM_THREADS_COUNT, threadsCount
            );
        } catch (Exception ex) {
            LOGGER.error("OK, We got that exception: " + ex.getMessage(), ex);
        }
    }
}
