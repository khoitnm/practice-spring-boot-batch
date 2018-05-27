package org.tnmk.practice.batch.partition.fileinput.jobs;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.tnmk.practice.batch.partition.fileinput.consts.JobParams;
import org.tnmk.practice.batch.partition.fileinput.consts.PartitionContextParams;
import org.tnmk.practice.batch.partition.fileinput.fileprocessor.UserLineMapperFactory;
import org.tnmk.practice.batch.partition.fileinput.fileprocessor.UserProcessor;
import org.tnmk.practice.batch.partition.fileinput.model.User;
import org.tnmk.practice.batch.partition.fileinput.partition.RangePartitioner;
import org.tnmk.practice.batch.partition.fileinput.tasklet.FanInTasklet;

/**
 * Tasklet and Chunk explanation:
 * http://www.baeldung.com/spring-batch-tasklet-chunk
 * https://stackoverflow.com/questions/40041334/difference-between-step-tasklet-and-chunk-in-spring-batch
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {
    public static final Logger log = LoggerFactory.getLogger(FanInTasklet.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileProcessingBatchJob() {
        return jobBuilderFactory.get("fileProcessingBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(fanOutStep())
                .next(fanInStep())
                .build();
    }

    /**
     * This is the master step.
     * View diagram in this site: http://www.baeldung.com/spring-batch-partitioner
     *
     * @return
     */
    @Bean
    public Step fanOutStep() {
        return stepBuilderFactory.get("fanOutStep")
                .partitioner(stepSlave().getName(), rangePartitioner())
                .partitionHandler(masterSlaveHandler())
                .build();
    }

    @Bean
    @JobScope
    public PartitionHandler masterSlaveHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(13);//The number of parallelling partitions which will be processed concurrently.
        handler.setTaskExecutor(partitionHandlerTaskExecutor());
        handler.setStep(stepSlave());
//      handler.afterPropertiesSet();
        return handler;
    }

    @Bean(name = "slave")
    public Step stepSlave() {
        log.info("...........called stepSlave .........");

        return stepBuilderFactory.get("stepSlave")
                //Enable chunk model, it means each step will include a Reader, Processor and Writer processes.
                .<User, User>chunk(4)//Each thread will process 4 items before sleeping so that other threads could be processed.
                .reader(slaveReader(null, 0, 0))
                .processor(slaveProcessor())
                .writer(slaveWriter(null, 0, 0)).build();
    }

    @Bean
    @JobScope
    public RangePartitioner rangePartitioner() {
        return new RangePartitioner();
    }

    @Bean
    @JobScope
    public TaskExecutor partitionHandlerTaskExecutor() {
        // each time the slave step is repeated, that step will be executed in a different thread.
        // Without this task, the partitions will be run on the main thread.
        // It means that there's no concurrency processes.
        //
        // If you use ConcurrentTaskExecutor, each time the slave step is repeated, that step will not be run on the main thread.
        // But all repeated execution are all run on the same thread??? Maybe I still don't understand it yet?!
        // It means that there's no concurrency processes either.
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    @StepScope
    public UserProcessor slaveProcessor() {
        return new UserProcessor();
    }


    @Autowired
    private UserLineMapperFactory userLineMapperFactory;

    /**
     * Read more at this https://www.petrikainulainen.net/programming/spring-framework/spring-batch-tutorial-reading-information-from-a-file/
     *
     * @param fromRowIndex
     * @param toRowIndex
     * @return
     */
    @Bean
    @StepScope
    public ItemStreamReader<User> slaveReader(
            @Value("#{jobParameters[" + JobParams.PARAM_INPUT_FILE_PATH + "]}") final String inputFilePath,
            @Value("#{stepExecutionContext[" + PartitionContextParams.FROM_INDEX + "]}") final int fromRowIndex,
            @Value("#{stepExecutionContext[" + PartitionContextParams.TO_INDEX + "]}") final int toRowIndex) {
        int headerLines = 1;

        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(inputFilePath));
        reader.setLineMapper(userLineMapperFactory.constructLineMapper());
        reader.setCurrentItemCount(headerLines + fromRowIndex);
        reader.setMaxItemCount(headerLines + toRowIndex + 1);//Doesn't include the last item

        return reader;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<User> slaveWriter(
            @Value("#{jobParameters[" + JobParams.PARAM_OUTPUT_FILE_PATH + "]}") final String outputFilePath,
            @Value("#{stepExecutionContext[" + PartitionContextParams.FROM_INDEX + "]}") final int fromId,
            @Value("#{stepExecutionContext[" + PartitionContextParams.TO_INDEX + "]}") final int toId) {

        FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(outputFilePath + fromId + "-" + toId + ".csv"));
        //writer.setAppendAllowed(false);
        writer.setLineAggregator(new DelimitedLineAggregator<User>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<User>() {{
                setNames(new String[]{"id", "username", "password", "age"});
            }});
        }});
        return writer;
    }


    @Bean
    public Step fanInStep() {
        return stepBuilderFactory.get("fanInStep").tasklet(fanInTask()).build();
    }

    @Bean
    public FanInTasklet fanInTask() {
        return new FanInTasklet();
    }
}
