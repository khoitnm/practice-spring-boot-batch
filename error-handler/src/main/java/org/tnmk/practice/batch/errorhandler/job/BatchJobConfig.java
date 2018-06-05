package org.tnmk.practice.batch.errorhandler.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.tnmk.common.batch.step.SaveItemsToStepContextWriter;
import org.tnmk.common.batch.step.FileItemReaderFactory;
import org.tnmk.practice.batch.errorhandler.consts.JobParams;
import org.tnmk.practice.batch.errorhandler.job.step.FanInTasklet;
import org.tnmk.practice.batch.errorhandler.job.step.StepContextItems;
import org.tnmk.practice.batch.errorhandler.job.step.UserProcessor;
import org.tnmk.practice.batch.errorhandler.model.User;

import java.util.Arrays;
import java.util.List;


/**
 * Tasklet and Chunk explanation:
 * http://www.baeldung.com/spring-batch-tasklet-chunk
 * https://stackoverflow.com/questions/40041334/difference-between-step-tasklet-and-chunk-in-spring-batch
 */
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
                .start(fanOutStep(null, null))
                .next(errorHandler())
                .build();
    }

    @JobScope
    @Bean
    public Step fanOutStep(
            @Value("#{jobParameters[" + JobParams.PARAM_CHUNK_SIZE + "]}") final Integer chunkSize,
            @Value("#{jobParameters[" + JobParams.PARAM_THREADS_COUNT + "]}") final Integer threadsCount) {
        return stepBuilderFactory.get("fan-out processing step")
                .listener(executionListenerSupport())
                .<User, User>chunk(chunkSize)
                .reader(fileReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())

                //Each chunk will run on a separated thread
                //There's only maximum 10 concurrent chunks are handled at the same time.
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .throttleLimit(threadsCount)
                .build();
    }

    @StepScope
    @Bean
    public StepExecutionListenerSupport executionListenerSupport() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{StepContextItems.STEP_KEY_ITEMS});
        return listener;
    }

    @JobScope
    @Bean
    public Step errorHandler() {
        return stepBuilderFactory.get("fan-in step")
                //IMPORTANT: If we new instance of FanInTasklet here instead of declaring a bean, it will cause Thread Lock Exception.
                .tasklet(fanInTasklet())
                .build();
    }

    //I can use JobScope, StepScope, or even SingletonScope here, the program runs just fine?!
    @Bean
//    @JobScope
    public FanInTasklet<List<User>> fanInTasklet(){
        return new FanInTasklet<List<User>>(StepContextItems.STEP_KEY_ITEMS);
    }

    /**
     * Read more at this https://www.petrikainulainen.net/programming/spring-framework/spring-batch-tutorial-reading-information-from-a-file/
     *
     * @return
     */
    @Bean
    @StepScope
    public FlatFileItemReader<User> fileReader(@Value("#{jobParameters[" + JobParams.PARAM_INPUT_FILE_PATH + "]}") final String inputFilePath) {
        return FileItemReaderFactory.constructItemStreamReader(
                inputFilePath,
                Arrays.asList("id", "username", "password", "age"), ",",
                User.class,
                0, -1);
    }

    @Bean
    @StepScope
    public ItemProcessor<User, User> itemProcessor() {
        return new UserProcessor();
    }


    /**
     * IMPORTANT: Don't return interface ItemWriter<> here because it make cause @BeforeStep doesn't work:
     * https://stackoverflow.com/questions/21241683/spring-batch-beforestep-does-not-work-with-stepscope
     *
     * @return
     */
    @Bean
    @StepScope
    public SaveItemsToStepContextWriter<User> itemWriter() {
        return new SaveItemsToStepContextWriter<>(StepContextItems.STEP_KEY_ITEMS);
    }
}
