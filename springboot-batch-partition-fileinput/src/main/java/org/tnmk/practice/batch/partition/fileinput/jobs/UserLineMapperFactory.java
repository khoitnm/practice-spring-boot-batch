package org.tnmk.practice.batch.partition.fileinput.jobs;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.stereotype.Component;
import org.tnmk.practice.batch.partition.fileinput.model.User;

@Component
public class UserLineMapperFactory {

    public LineMapper<User> constructLineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(constructLineTokenizer());
        lineMapper.setFieldSetMapper(constructFieldSetMapper());
        return lineMapper;
    }

    private LineTokenizer constructLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames(new String[]{"id", "username", "password", "age"});
        return lineTokenizer;
    }

    private FieldSetMapper<User> constructFieldSetMapper() {
        BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(User.class);
        return beanWrapperFieldSetMapper;
    }
}
