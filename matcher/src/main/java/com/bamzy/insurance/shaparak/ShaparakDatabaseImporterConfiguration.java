package com.bamzy.insurance.shaparak;

import com.bamzy.insurance.model.ShaparakRecord;
import com.bamzy.insurance.model.ShaparakRecordRowMapper;
import com.bamzy.insurance.model.Repository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by jalil on 1/26/2015.
 */
@Configuration
public class ShaparakDatabaseImporterConfiguration {
    @Bean
    public ItemReader<ShaparakRecord> shaparakDatabaseReader(DataSource datasource) {
        JdbcCursorItemReader<ShaparakRecord> reader = new JdbcCursorItemReader<ShaparakRecord>();
        reader.setSql("select * from shaparakData where status is null or status not like 'matched'");
        reader.setRowMapper(new ShaparakRecordRowMapper());
        reader.setDataSource(datasource);
        return reader;

    }

    @Bean
    public ItemProcessor<ShaparakRecord, ShaparakRecord> shaparakDatabaseProcessor() {
        return new ShaparakRecordItemProcessor();
    }

    @Bean(name = "shaparakDatabaseWriter")
    public ItemWriter<ShaparakRecord> writer(final Repository repository) {
        ItemWriter<ShaparakRecord> writer = new ItemWriter<ShaparakRecord>() {
            @Override
            public void write(List<? extends ShaparakRecord> items) throws Exception {

                for (ShaparakRecord item : items) {
                    repository.insertShaparakRecord(item);
                }
            }
        };
        return writer;
    }

    @Bean(name = "shaparakDatabaseJob")
    public Job importUserJob(JobBuilderFactory jobs, @Qualifier(value = "shaparakDatabaseStep") Step s1) {
        return jobs.get("ShaparakDatabaseImportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean(name = "shaparakDatabaseStep")
    public Step step1(StepBuilderFactory stepBuilderFactory,
                      @Qualifier(value = "shaparakDatabaseWriter") ItemWriter<ShaparakRecord> writer,
                      @Qualifier("shaparakDatabaseReader") ItemReader<? extends ShaparakRecord> reader) {
        return stepBuilderFactory.get("shaparakDatabaseStep")
                .<ShaparakRecord, ShaparakRecord>chunk(10)
                .reader(reader)
                .processor(shaparakDatabaseProcessor())
                .writer(writer)
                .build();
    }
}
