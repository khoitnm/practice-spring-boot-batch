package org.tnmk.practice.batch.simplesteps.concurrencyjobs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tnmk.practice.batch.simplesteps.concurrencyjobs.joblauncher.FileProcessingJobLauncherHelper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunJobsInSequenceApplicationTests {

    @Autowired
    FileProcessingJobLauncherHelper fileProcessingJobLauncherHelper;

    @Test
    public void startFileProcessingBatchJob() throws InterruptedException, ExecutionException {
        fileProcessingJobLauncherHelper.startFileProcessJob("/users_7K.csv", "out/csv_7K/users.processed.");
        fileProcessingJobLauncherHelper.startFileProcessJob("/users.csv", "out/csv/users.processed.");
    }

}
