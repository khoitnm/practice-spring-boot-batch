package org.tnmk.practice.batch.chunkmodel.concurrencyjobs.job.step;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

public class FileItemReaderFactory {

    public static <T> FlatFileItemReader<T> constructItemStreamReader(final String inputFilePath, List<String> fileColumns, Class<T> itemClazz, final int fromRowIndex, final int toRowIndex) {
        int headerLines = 1;

        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(inputFilePath));
        reader.setLineMapper(LineMapperFactory.constructReaderLineMapper(fileColumns, itemClazz));
        if (fromRowIndex >= 0) {
            reader.setCurrentItemCount(headerLines + fromRowIndex);
        }
        if (toRowIndex >= 0) {
            reader.setMaxItemCount(headerLines + toRowIndex + 1);//Doesn't include the last item
        }
        return reader;
    }
}
