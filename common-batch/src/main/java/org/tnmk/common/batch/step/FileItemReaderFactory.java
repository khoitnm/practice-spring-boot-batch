package org.tnmk.common.batch.step;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

public class FileItemReaderFactory {
    public static final int HEADER_LINES = 1;

    public static <T> FlatFileItemReader<T> constructItemStreamReader(final String inputFilePath, List<String> fileColumns, String columnDelimiter, Class<T> itemClazz, final int fromRowIndex, final int toRowIndex) {
        int headerLines = HEADER_LINES;

        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(inputFilePath));
        reader.setLineMapper(LineMapperFactory.constructReaderLineMapper(fileColumns, columnDelimiter, itemClazz));
        if (fromRowIndex >= 0) {
            reader.setCurrentItemCount(headerLines + fromRowIndex);
        }
        if (toRowIndex >= 0) {
            reader.setMaxItemCount(headerLines + toRowIndex + 1);//Doesn't include the last item
        }
        return reader;
    }
}
