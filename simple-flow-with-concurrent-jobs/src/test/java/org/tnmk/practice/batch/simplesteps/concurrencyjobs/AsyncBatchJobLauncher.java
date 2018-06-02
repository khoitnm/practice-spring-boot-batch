package org.tnmk.practice.batch.simplesteps.concurrencyjobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.tnmk.practice.batch.simplesteps.concurrencyjobs.joblauncher.FileProcessingJobLauncherHelper;

import java.util.concurrent.Future;

/**
 * I just used this class for simulate the async requests.
 */
@Service
public class AsyncBatchJobLauncher {

    @Autowired
    FileProcessingJobLauncherHelper fileProcessingJobLauncherHelper;

    @Async
    public Future<String> run(String inputFilePath, String outputPathWithFilePrefix) {
        fileProcessingJobLauncherHelper.startFileProcessJob(inputFilePath, outputPathWithFilePrefix);
        return new AsyncResult<>("Finish " + outputPathWithFilePrefix);
    }
}
