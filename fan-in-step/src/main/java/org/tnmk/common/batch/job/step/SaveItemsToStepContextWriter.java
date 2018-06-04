package org.tnmk.common.batch.job.step;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveItemsToStepContextWriter<ITEM> implements ItemWriter<ITEM> {
    private final String itemsKeyInStep;

    private StepExecution stepExecution;

    public SaveItemsToStepContextWriter(String itemsKeyInStep) {
        this.itemsKeyInStep = itemsKeyInStep;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends ITEM> items) {
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        List<ITEM> list = (List<ITEM>) stepContext.get(itemsKeyInStep);
        if (list != null) {
            list.addAll(items);
        } else {
            list = Collections.synchronizedList(new ArrayList<>(items));
        }
        stepContext.put(itemsKeyInStep, list);
    }


}
