package org.tnmk.practice.batch.partition.fileinput;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tnmk.practice.batch.partition.fileinput.joblauncher.JobLauncherWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBatchPartitionApplicationTests {

    @Autowired
    JobLauncherWrapper jobLauncherWrapper;

    @Autowired
    private Job batchJob;

    @Test
    public void contextLoads() {
        jobLauncherWrapper.startJob(batchJob);
    }

}
