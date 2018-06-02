package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.consts.JobParams;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.step.FileItemReaderFactory;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.step.FileItemWriterFactory;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.step.UserProcessor;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.model.User;

import javax.batch.runtime.context.StepContext;
import java.util.Arrays;


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

    @JobScope
    @Bean
    public Step simpleStep() {
        return stepBuilderFactory.get("user step")
                .<User, User>chunk(5)
                .reader(fileReader(null))
                .processor(itemProcessor())
                .writer(fileWriter(null))
                .build();
    }

    /**
     * Read more at this https://www.petrikainulainen.net/programming/spring-framework/spring-batch-tutorial-reading-information-from-a-file/
     *
     * @return
     */
    @Bean
    @StepScope
    public ItemStreamReader<User> fileReader(@Value("#{jobParameters[" + JobParams.PARAM_INPUT_FILE_PATH + "]}") final String inputFilePath) {
        return FileItemReaderFactory.constructItemStreamReader(
                inputFilePath,
                Arrays.asList("id", "username", "password", "age"),
                User.class,
                1, -1);
    }

    @Bean
    @StepScope
    public ItemProcessor<User, User> itemProcessor(){
        return new UserProcessor();
    }

    @Bean
    @StepScope
    public ItemStreamWriter<User> fileWriter(@Value("#{jobParameters[" + JobParams.PARAM_OUTPUT_FILE_PATH + "]}") final String outputFilePath) {

        return FileItemWriterFactory.constructFileItemWriter(
                outputFilePath,
                Arrays.asList("id", "username", "password", "age"),
                1, -1);
    }
}
