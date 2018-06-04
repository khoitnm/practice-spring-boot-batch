package org.tnmk.practice.batch.faninstep.job.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.tnmk.common.batch.jobparam.JobParams;
import org.tnmk.practice.batch.faninstep.job.BatchJobConfig;

import java.util.Map;

public class FanInTasklet<T> implements Tasklet {
    public static final Logger log = LoggerFactory.getLogger(BatchJobConfig.class);

    private final String stepContextParamName;

    public FanInTasklet(String stepContextParamName) {
        this.stepContextParamName = stepContextParamName;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobInstance = (String) chunkContext.getStepContext().getJobParameters().get(JobParams.PARAM_JOB_INSTANCE_ID);
        Map<String, Object> jobContext = chunkContext.getStepContext().getJobExecutionContext();
        T stepContextParamValue = (T) jobContext.get(stepContextParamName);

        log.info("Thread[{}]: Executing FanInStep: JobInstance: {}, Step contribution: {}, chunkContext: {}", Thread.currentThread().getName(), jobInstance, contribution, chunkContext);
        System.out.println("Thread[" + Thread.currentThread().getName() + "] StepContextParamValue: " + stepContextParamValue);
        Thread.sleep(1000);
//        return RepeatStatus.FINISHED;
        return null;
    }
}