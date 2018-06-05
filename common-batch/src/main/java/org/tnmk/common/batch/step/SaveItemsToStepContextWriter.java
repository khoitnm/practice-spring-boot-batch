package org.tnmk.common.batch.step;

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
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        List<ITEM> list = (List<ITEM>) stepContext.get(itemsKeyInStep);
        // Note: As mentioned in write(...) method, always initiate an empty list here.
        // Never initiate an empty list in {@link write(...) method to avoid hidden error because of multi-thread.
        if (list == null) {
            list = Collections.synchronizedList(new ArrayList<>());//The list must be thread-safe.
            stepContext.put(itemsKeyInStep, list);
        }
    }

    @Override
    public void write(List<? extends ITEM> items) {
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        List<ITEM> list = (List<ITEM>) stepContext.get(itemsKeyInStep);
        list.addAll(items);

        //Important note to avoid hidden bug:
        // You may think about initiating an empty list if it's is null here, but it will cause hidden error because of multi-thread.
        // the problem could happen when writer threads running concurrently, the checking null could happen twice or more!!!
        // It was fixed by moving the list initiation to beforeStep block which is executed before any thread are processed.
        // Read the list initiation code in the {@link beforeStep()} to understand more.
    }


}
