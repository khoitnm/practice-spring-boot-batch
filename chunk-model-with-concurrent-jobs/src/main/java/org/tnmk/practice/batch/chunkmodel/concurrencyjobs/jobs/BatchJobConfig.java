package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Tasklet and Chunk explanation:
 * http://www.baeldung.com/spring-batch-tasklet-chunk
 * https://stackoverflow.com/questions/40041334/difference-between-step-tasklet-and-chunk-in-spring-batch
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileProcessingBatchJob() {
        return jobBuilderFactory.get("fileProcessingBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(simpleStep())
                .build();
    }

    @Bean
    public Step simpleStep() {
        return stepBuilderFactory.get("SimpleTasklet step").tasklet(new SimpleTasklet()).build();
    }

}
