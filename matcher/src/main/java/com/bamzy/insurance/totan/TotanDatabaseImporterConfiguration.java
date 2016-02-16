package com.bamzy.insurance.totan;

import com.bamzy.insurance.model.Repository;
import com.bamzy.insurance.model.TotanRecord;
import com.bamzy.insurance.model.TotanRecordRowMapper;
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
import org.springframework.dao.DuplicateKeyException;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by jalil on 1/29/2015.
 */
@Configuration
public class TotanDatabaseImporterConfiguration {

    @Bean
    public ItemReader<TotanRecord> totanDatabaseReader(DataSource datasource) {
        JdbcCursorItemReader<TotanRecord> reader = new JdbcCursorItemReader<TotanRecord>();
        reader.setSql("select * from totanData where status is null or status not like 'matched'");
        reader.setRowMapper(new TotanRecordRowMapper());
        reader.setDataSource(datasource);
        return reader;

    }

    @Bean
    public ItemProcessor<TotanRecord, TotanRecord> totanDatabaseProcessor() {
        return new TotanRecordItemProcessor();
    }

    @Bean(name = "totanDatabaseWriter")
    public ItemWriter<TotanRecord> writer(final Repository repository) {
        return new ItemWriter<TotanRecord>() {
            @Override
            public void write(List<? extends TotanRecord> items) throws Exception {

                for (TotanRecord item : items) {
                    // ignoring totan duplicate keys
                    try {
                        repository.insertTotanRecord(item);
                    } catch (DuplicateKeyException ignored) {
                    }
                }
            }
        };
    }

    @Bean(name = "totanDatabaseJob")
    public Job importUserJob(JobBuilderFactory jobs, @Qualifier("totanDatabaseStep") Step s1) {
        return jobs.get("TotanDatabaseImportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean(name = "totanDatabaseStep")
    public Step step1(StepBuilderFactory stepBuilderFactory,
                      @Qualifier("totanDatabaseWriter") ItemWriter<TotanRecord> writer,
                      @Qualifier("totanDatabaseReader") ItemReader<? extends TotanRecord> reader) {
        return stepBuilderFactory.get("totanDatabaseStep")
                .<TotanRecord, TotanRecord>chunk(10)
                .reader(reader)
                .processor(totanDatabaseProcessor())
                .writer(writer)
                .build();
    }
}
