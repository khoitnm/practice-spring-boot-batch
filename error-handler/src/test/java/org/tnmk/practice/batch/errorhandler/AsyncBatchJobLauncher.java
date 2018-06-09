package org.tnmk.practice.batch.errorhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.tnmk.practice.batch.errorhandler.joblauncher.FileProcessingJobLauncherHelper;

import java.util.concurrent.Future;

/**
 * I just used this class for simulate the async requests.
 */
@Service
public class AsyncBatchJobLauncher {

    @Autowired
    FileProcessingJobLauncherHelper fileProcessingJobLauncherHelper;

    @Async
    public Future<String> run(String inputFilePath, int chunkSize, int threadsCount) {
        fileProcessingJobLauncherHelper.startFileProcessJob(inputFilePath, chunkSize, threadsCount);
        return new AsyncResult<>("Finish " + inputFilePath);
    }
}
