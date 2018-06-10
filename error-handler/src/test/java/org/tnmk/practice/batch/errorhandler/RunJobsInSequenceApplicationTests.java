package org.tnmk.practice.batch.errorhandler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tnmk.practice.batch.errorhandler.joblauncher.FileProcessingJobLauncherHelper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunJobsInSequenceApplicationTests {

    @Autowired
    FileProcessingJobLauncherHelper fileProcessingJobLauncherHelper;

    @Test
    public void startFileProcessingBatchJob() {
//        fileProcessingJobLauncherHelper.startFileProcessJob("/not_exist_file.csv", 3, 2);
//        fileProcessingJobLauncherHelper.startFileProcessJob("/heroes_error_at_3.csv", 2, 2);
        fileProcessingJobLauncherHelper.startFileProcessJob("/heroes_error_at_3_4.csv", 3, 2);
    }

}
