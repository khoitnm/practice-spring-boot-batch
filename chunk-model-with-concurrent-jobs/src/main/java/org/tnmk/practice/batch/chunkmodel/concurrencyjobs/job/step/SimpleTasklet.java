package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.BatchJobConfig;

public class SimpleTasklet implements Tasklet {
    public static final Logger log = LoggerFactory.getLogger(BatchJobConfig.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobInstance = (String) chunkContext.getStepContext().getJobParameters().get(org.tnmk.common.batch.constants.JobParams.PARAM_JOB_INSTANCE_ID);
        log.info("Thread[{}]: Executing SimpleTasklet: JobInstance: {}, Step contribution: {}, chunkContext: {}", Thread.currentThread().getName(), jobInstance, contribution, chunkContext);
        Thread.sleep(1000);
        return RepeatStatus.FINISHED;
    }
}
