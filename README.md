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
It can run concurrent jobs with a very simple step (just writing log). 

4. `chunk-model-with-concurrent-jobs`: 
It can run concurrent jobs which reading data from different files. 
Each job will have a processing step which runs with multi threads.

Build project with Gradlew:
```
sh gradlew clean build
```
or if you want to skip tests, run:
```
sh gradlew clean build -x test
```

