You can import the whole project with all modules into your IDE, or you can import some modules separately.

1. `partition-dbinput`: This project will read multiple lines from DB in parallel and then write data into separated csv files.
Some applied concepts: 
    + Partition with master-slave steps.
    + Chunk model with: Reader, Writer, Processor
    + **Warn**: it may have some problem when running concurrent jobs, I have not checked it yet.

2. `partition-dbinput`: This project will read multiple lines from DB in parallel and then write data into separated csv files.
Some applied concepts: 
    + Partition with master-slave steps.
    + Chunk model with: Reader, Writer, Processor
    + **Warn**: it has some problem when running concurrent jobs

3. `simple-flow-with-concurrent-jobs`: 
It can run concurrent jobs. Each job will run a very simple step (just writing log). 

4. `chunk-model-with-concurrent-jobs`: 
It can run concurrent jobs which reading data from different files.<br/>
Each job will read rows in a separated file.<br/>
Those rows will be read concurrently in many threads.<br/>
And then those rows will be written back to different files, each thread will write rows to a separated file.<br/>

5. `fan-in-step`:
Similar to `chunk-model-with-concurrent-job`, this module will read rows in file concurrently in multi-thread (fan-out step).<br/>
After all threads are finished, we will do something with a all processed rows (this step is called fan-in-step).<br/>
This demo will show how to pass the result of concurrent threads from fan-out-step to the fan-in-step.<br/>
However, this demo will not write processed rows to any file, it just write logs in the fan-in-step.

## Build
You can build project with `gradlew` (if you already install gradle in your local machine, you can run with `gradle` instead of `gradlew`):
```
sh gradlew clean build
```
or if you want to skip tests, run:
```
sh gradlew clean build -x test
```


## Additional References
1. https://spring.io/guides
2. https://spring.io/guides/gs/batch-processing/
3. https://spring.io/guides/gs/yarn-batch-restart/
4. https://spring.io/guides/gs/yarn-batch-processing/
5. Good diagrams
    http://www.desynit.com/dev-zone/java/breaking-a-spring-batch-job-into-smaller-steps/ 

6. Compare the learning curve and features of Spring Batch vs. EasyBatch
    https://benas.github.io/2014/03/03/spring-batch-vs-easy-batch-feature-comparison.html 
7. Passing data to further steps
    https://docs.spring.io/spring-batch/trunk/reference/html/patterns.html#passingDataToFutureSteps
8. Spring Batch Samples: 
    https://github.com/spring-projects/spring-batch/tree/master/spring-batch-samples#partitioning-sample    


