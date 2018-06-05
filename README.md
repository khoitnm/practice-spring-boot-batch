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

## TODO
+ Repeat step when cannot reading file (maybe because of some network error)
+ Error handler.

## Additional References
1. https://spring.io/guides
2. https://spring.io/guides/gs/batch-processing/
3. Restart step:<br/>
    https://spring.io/guides/gs/yarn-batch-restart/ <br/>
    https://stackoverflow.com/questions/35782041/spring-batch-repeats-step-with-chunk-size-1-even-after-success <br/>
4. https://spring.io/guides/gs/yarn-batch-processing/
5. Good diagrams
    http://www.desynit.com/dev-zone/java/breaking-a-spring-batch-job-into-smaller-steps/ 

6. Compare the learning curve and features of Spring Batch vs. EasyBatch
    https://benas.github.io/2014/03/03/spring-batch-vs-easy-batch-feature-comparison.html 
7. Passing data to further steps
    https://docs.spring.io/spring-batch/trunk/reference/html/patterns.html#passingDataToFutureSteps
8. Spring Batch Samples: 
    https://github.com/spring-projects/spring-batch/tree/master/spring-batch-samples#partitioning-sample
9. Suggest don't need @Bean and @StepScope for Step, Reader, Processor, Writer. But in that case, you have to add `step.afterPropertiesSet()` after instantiate those objects.
    It also shows how to run many steps concurrently.
    https://stackoverflow.com/questions/37238813/spring-batch-looping-a-reader-processor-writer-step/37271735#37271735
10. Generate steps dynamically
    https://stackoverflow.com/questions/37310658/spring-batch-how-to-generate-parallel-steps-based-on-params-created-in-a-previ/37315830#37315830    
11. Skip step when error and go to next step:
    https://stackoverflow.com/questions/39134634/spring-batch-java-config-skip-step-when-exception-and-go-to-next-steps/39143725#39143725    
12. Scaling with remote partitioning.
    https://asardana.com/2018/01/01/scaling-spring-batch-on-aws-with-remote-partitioning/   
13. Error Handling
    https://terasoluna-batch.github.io/guideline/5.0.0.RELEASE/en/Ch06_ExceptionHandling.html
14. Spring Batch Admin
    https://docs.spring.io/spring-batch-admin/trunk/
    https://docs.spring.io/spring-batch-admin/trunk/reference/reference.xhtml         
        


