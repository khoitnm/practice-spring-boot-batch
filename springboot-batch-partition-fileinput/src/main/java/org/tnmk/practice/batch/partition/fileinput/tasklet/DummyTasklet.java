package org.tnmk.practice.batch.partition.fileinput.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class DummyTasklet implements Tasklet {
    public static final Logger logger = LoggerFactory.getLogger(DummyTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        logger.info("Dummy Tasklet called.");
        return RepeatStatus.FINISHED;
    }
}
