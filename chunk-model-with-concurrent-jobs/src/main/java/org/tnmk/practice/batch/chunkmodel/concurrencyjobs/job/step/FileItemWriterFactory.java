package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.step;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.tnmk.practice.batch.chunkmodel.concurrencyjobs.model.User;

import java.util.List;

public class FileItemWriterFactory {
    public static <T> FlatFileItemWriter<T> constructFileItemWriter(String outputFilePath, List<String> columnNames, int fromId, int toId) {

        FlatFileItemWriter<T> writer = new FlatFileItemWriter<>();

        StringBuilder finalOutputFilePathBuilder = new StringBuilder(outputFilePath);
        if (fromId >= 0) {
            finalOutputFilePathBuilder.append("_").append(fromId);
        }
        if (toId >= 0) {
            finalOutputFilePathBuilder.append("-").append(toId);

        }
        finalOutputFilePathBuilder.append(".csv");

        writer.setResource(new FileSystemResource(finalOutputFilePathBuilder.toString()));
        //writer.setAppendAllowed(false);
        writer.setLineAggregator(new DelimitedLineAggregator<T>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<T>() {{
                setNames(columnNames.toArray(new String[0]));
            }});
        }});
        return writer;
    }
}
