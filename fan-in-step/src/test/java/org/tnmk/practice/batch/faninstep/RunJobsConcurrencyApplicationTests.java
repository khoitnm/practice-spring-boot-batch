package org.tnmk.practice.batch.faninstep;

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
        Future<String> future = asyncBatchJobLauncher.run("/heroes_5.csv", "out/csv_5/heroes_5_1.processed.");
        Future<String> future2 = asyncBatchJobLauncher.run("/heroes_5.csv", "out/csv_10/heroes_5_2.processed.");
        Future<String> future3 = asyncBatchJobLauncher.run("/heroes_5.csv", "out/csv_5/heroes_5_3.processed.");
        Future<String> future4 = asyncBatchJobLauncher.run("/heroes_5.csv", "out/csv_5/heroes_5_4.processed.");

        while (true) {
            if (future.isDone() && future2.isDone() && future3.isDone() && future4.isDone()) {
                System.out.println("Result from asynchronous process - " + future.get() + "& " + future2.get());
                break;
            }
            System.out.println("Continue doing something else. ");
            Thread.sleep(1000);
        }

    }

}
