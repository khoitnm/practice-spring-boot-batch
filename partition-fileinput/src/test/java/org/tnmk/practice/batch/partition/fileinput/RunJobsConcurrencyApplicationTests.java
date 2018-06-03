package org.tnmk.practice.batch.partition.fileinput;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunJobsConcurrencyApplicationTests {

    @Autowired
    AsyncBatchJobLauncher asyncBatchJobLauncher;

    @Test
    public void startContext(){
    }

    //TODO At this moment, somehow running 2 job instances concurrency get stuck!!!
//    @Test
    public void startFileProcessingBatchJob() throws InterruptedException, ExecutionException {
        Future<String> future = asyncBatchJobLauncher.run("/users.csv", "out/csv/users.processed.");
        Future<String> future2 = asyncBatchJobLauncher.run("/users_7K.csv", "out/csv_7K/users.processed.");

        while (true) {
            if (future.isDone() && future2.isDone()) {
                System.out.println("Result from asynchronous process - " + future.get() + "& " + future2.get());
                break;
            }
            System.out.println("Continue doing something else. ");
            Thread.sleep(1000);
        }

    }

}
