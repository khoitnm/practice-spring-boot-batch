package org.tnmk.practice.batch.errorhandler.job.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.tnmk.common.batch.jobparam.JobParams;
import org.tnmk.practice.batch.errorhandler.job.BatchJobConfig;

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

        log.info("" +
            "\nFAN-IN-TASKLET:" +
            "\n\tThread: " + Thread.currentThread().getName() +
            "\n\tJobInstance: " + jobInstance +
            "\n\tStep contribution: " + contribution +
            "\n\tContextParamValue from Previous Step: " + stepContextParamValue +
            "\n\tChunkContext: " + chunkContext
        );
        Thread.sleep(1000);
        return null;
    }
}