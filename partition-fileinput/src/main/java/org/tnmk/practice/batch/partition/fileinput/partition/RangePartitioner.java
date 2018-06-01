package org.tnmk.practice.batch.partition.fileinput.partition;

import java.util.HashMap;
import java.util.Map;

import org.tnmk.practice.batch.partition.fileinput.consts.PartitionContextParams;
import org.tnmk.practice.batch.partition.fileinput.tasklet.FanInTasklet;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

@Slf4j
public class RangePartitioner implements Partitioner {
    public static final Logger log = LoggerFactory.getLogger(FanInTasklet.class);


    /**
     * @param gridSize the number of partition
     * @return
     */
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        log.info("partition called gridsize= " + gridSize);

        Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
        //pageSize could be calculated based on number of lines: https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
        int pageSize = 50;
        int iFromId = 0;
        int iToId = iFromId + pageSize - 1;

        for (int i = 0; i <= gridSize; i++) {
            ExecutionContext iPartitionContext = new ExecutionContext();
            iPartitionContext.putString(PartitionContextParams.PARTITION_NAME, "Partition " + i);
            iPartitionContext.putInt(PartitionContextParams.FROM_INDEX, iFromId);
            iPartitionContext.putInt(PartitionContextParams.TO_INDEX, iToId);

            result.put("partition" + i, iPartitionContext);

            iFromId = iToId + 1;
            iToId += pageSize;
        }
        return result;
    }
}
