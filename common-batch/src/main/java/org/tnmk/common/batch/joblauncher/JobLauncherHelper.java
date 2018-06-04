package org.tnmk.common.batch.joblauncher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.tnmk.common.batch.exception.BatchJobException;
import org.tnmk.common.batch.jobparam.JobParams;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This is just a helper class to start a job more easier.
 *
 * This class looks a little bit complicated, but its idea is very simple.
 * It just provide you a convenient way to starting a job with the {@link Map} or {@link Object[]} parameter instead of an instance of {@link JobParameters}.
 */
@Service
public class JobLauncherHelper {
    public static final Logger logger = LoggerFactory.getLogger(JobLauncherHelper.class);

    @Autowired
    private JobLauncher jobLauncher;

    /**
     * @param job
     * @param params
     * This is actually just a convenient way to start a job with {@link Map} parameters.<br/>
     * The odd element will be the parameter name, and the even element will be the parameter value.<br/>
     * <pre>
     * <b>For example:</b>
     * <ul>
     *     <li><code>startJob(job, "paramName1", "paramValue1", "paramName2", "paramValue2")</code></li>
     *     <li><code>startJob(job, "username", "kevin.tran", "age", 99, "description","a live long funny guy!")</code></li>
     * </ul>
     * </pre>
     * That's why the number of elements in params should be even.<br/>
     */
    public void startJob(Job job, Object... params) {
        Map<String, Object> paramsMap = toParamsMap(params);
        startJob(job, paramsMap);
    }

    public void startJob(Job job, Map<String, Object> params) {
        JobParameters jobParameters = toJobParameters(params);
        startJobWithParameters(job, jobParameters);
    }

    private Map<String, Object> toParamsMap(Object... params) {
        Map<String, Object> paramsMap = new HashMap<>(params.length / 2);
        String paramKey = null;
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (i % 2 == 0) {
                paramKey = (String) param;
            } else {
                Object paramValue = param;
                paramsMap.put(paramKey, paramValue);
            }
        }
        return paramsMap;
    }

    private JobParameters toJobParameters(Map<String, Object> params) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                // The job instance is determine by Job & JobParameters.
                // This param ensure that each jobInstance will have a different Id.
                .addString(JobParams.PARAM_JOB_INSTANCE_ID, UUID.randomUUID().toString(), true);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object entryValue = entry.getValue();
            if (entryValue == null) {
                continue;
            }
            if (entryValue instanceof String) {
                jobParametersBuilder.addString(entry.getKey(), (String) entryValue);
            } else if (entryValue.getClass().isAssignableFrom(Number.class)) {
                Number numVal = (Number) entryValue;
                if (entryValue.getClass().isAssignableFrom(Long.class) || entryValue.getClass().isAssignableFrom(Integer.class)) {
                    jobParametersBuilder.addLong(entry.getKey(), NumberUtils.convertNumberToTargetClass(numVal, Long.class));
                } else if (entryValue.getClass().isAssignableFrom(Float.class) || entryValue.getClass().isAssignableFrom(Double.class)) {
                    jobParametersBuilder.addDouble(entry.getKey(), NumberUtils.convertNumberToTargetClass(numVal, Double.class));
                } else if (entryValue instanceof Date) {
                    jobParametersBuilder.addDate(entry.getKey(), (Date) entryValue);
                }
            }
        }
        return jobParametersBuilder.toJobParameters();
    }

    private void startJobWithParameters(Job job, JobParameters jobParameters) {
        try {
            logger.info("Thread[{}]: Starting job {} /////////////////////////////////////////////////", Thread.currentThread().getName(), jobParameters);
            jobLauncher.run(job, jobParameters);
            logger.info("Thread[{}]: Finish starting job {} -----------------------------------------", Thread.currentThread().getName(), jobParameters);
        } catch (JobRestartException | JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new BatchJobException(e);
        }
    }
}
