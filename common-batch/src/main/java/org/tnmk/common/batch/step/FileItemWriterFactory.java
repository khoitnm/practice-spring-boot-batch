package org.tnmk.common.batch.step;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

public class FileItemWriterFactory {
    /**
     * @param outputFilePath
     * @param columnNames
     * @param columnDelimiter usually ","
     * @param fromId -1: no from Id, it means reading from the beginning.
     * @param toId -1: no limit
     * @param <T>
     * @return
     */
    public static <T> FlatFileItemWriter<T> constructFileItemWriter(String outputFilePath, List<String> columnNames, String columnDelimiter, int fromId, int toId) {

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
            setDelimiter(columnDelimiter);
            setFieldExtractor(new BeanWrapperFieldExtractor<T>() {{
                setNames(columnNames.toArray(new String[0]));
            }});
        }});
        return writer;
    }
}
