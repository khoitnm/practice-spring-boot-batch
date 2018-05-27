package org.tnmk.practice.batch.partition.fileinput;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tnmk.practice.batch.partition.fileinput.joblauncher.FileProcessingJobLauncherHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBatchPartitionApplicationTests {

    @Autowired
    FileProcessingJobLauncherHelper fileProcessingJobLauncherHelper;

    @Autowired
    private Job fileProcessingBatchJob;

    @Test
    public void startFileProcessingBatchJob() {
        fileProcessingJobLauncherHelper.startFileProcessJob("/users.csv");
    }

}
