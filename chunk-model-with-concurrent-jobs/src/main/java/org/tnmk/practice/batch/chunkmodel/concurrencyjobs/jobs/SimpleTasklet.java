package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.consts.JobParams;

public class SimpleTasklet implements Tasklet {
    public static final Logger log = LoggerFactory.getLogger(BatchJobConfig.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobInstance = (String) chunkContext.getStepContext().getJobParameters().get(JobParams.PARAM_JOB_INSTANCE_ID);
        log.info("Executing SimpleTasklet: JobInstance: {}, Step contribution: {}, chunkContext: {}", jobInstance, contribution, chunkContext);
        Thread.sleep(1000);
        return RepeatStatus.FINISHED;
    }
}
