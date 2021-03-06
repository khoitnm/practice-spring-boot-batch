package org.tnmk.common.batch.step;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LineMapperFactory {

    /**
     * @param columnNames
     * @param columnDelimiter usually ","
     * @param itemClazz
     * @param <T>
     * @return
     */
    public static <T> LineMapper<T> constructReaderLineMapper(List<String> columnNames, String columnDelimiter, Class<T> itemClazz) {
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(constructLineTokenizer(columnNames, columnDelimiter));
        lineMapper.setFieldSetMapper(constructFieldSetMapper(itemClazz));
        return lineMapper;
    }

    private static LineTokenizer constructLineTokenizer(List<String> columnNames, String columnDelimiter) {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(columnDelimiter);
        lineTokenizer.setNames(columnNames.toArray(new String[0]));
        return lineTokenizer;
    }

    private static <T> FieldSetMapper<T> constructFieldSetMapper(Class<T> itemClazz) {
        BeanWrapperFieldSetMapper<T> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(itemClazz);
        return beanWrapperFieldSetMapper;
    }
}
