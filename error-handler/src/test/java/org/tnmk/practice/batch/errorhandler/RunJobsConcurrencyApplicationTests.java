package org.tnmk.practice.batch.errorhandler;

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
    public void startFileProcessingBatchJob() throws InterruptedException, ExecutionException {
        Future<String> future = asyncBatchJobLauncher.run("/heroes_error_at_3.csv", 2, 3);
        Future<String> future2 = asyncBatchJobLauncher.run("/users_10.csv", 3, 3);
        Future<String> future3 = asyncBatchJobLauncher.run("/heroes_error_at_3.csv", 4, 3);
        Future<String> future4 = asyncBatchJobLauncher.run("/users_10.csv", 5, 3);

        while (true) {
            if (future.isDone() && future2.isDone() && future3.isDone() && future4.isDone()) {
                System.out.println("Result from asynchronous process.");
                break;
            }
            System.out.println("Continue doing something else. ");
            Thread.sleep(1000);
        }

    }

}
