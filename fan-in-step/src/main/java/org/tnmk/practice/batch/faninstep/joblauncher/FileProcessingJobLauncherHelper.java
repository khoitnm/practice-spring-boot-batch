package org.tnmk.practice.batch.faninstep.joblauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tnmk.practice.batch.faninstep.consts.JobParams;
import org.tnmk.common.batch.joblauncher.JobLauncherHelper;

import static org.tnmk.practice.batch.faninstep.consts.JobParams.PARAM_THREADS_COUNT;

/**
 * This is just a helper class to start a job more easier.
 */
@Service
public class FileProcessingJobLauncherHelper {
    @Autowired
    private JobLauncherHelper jobLauncherHelper;
    @Autowired
    private Job fileProcessingJob;

    public void startFileProcessJob(String inputFilePath, int chunkSize, int threadsCount) {
        jobLauncherHelper.startJob(fileProcessingJob,
                JobParams.PARAM_INPUT_FILE_PATH, inputFilePath,
                JobParams.PARAM_CHUNK_SIZE, chunkSize,
                JobParams.PARAM_THREADS_COUNT, threadsCount
        );
    }
}
