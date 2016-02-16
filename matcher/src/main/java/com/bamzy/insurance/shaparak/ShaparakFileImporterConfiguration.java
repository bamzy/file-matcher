package com.bamzy.insurance.shaparak;

import com.bamzy.insurance.model.ShaparakRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by jalil on 1/24/2015.
 */

@Configuration
public class ShaparakFileImporterConfiguration {

    @Bean(name = "shaparakFileReader")
    @Scope("step")
    public FlatFileItemReader<ShaparakRecord> shaparakReader(@Value("#{jobParameters['input.file.name']}") String filePath) throws MalformedURLException {
        URL url = new File(filePath).toURI().toURL();
        FlatFileItemReader<ShaparakRecord> reader = new FlatFileItemReader<ShaparakRecord>();
        reader.setLinesToSkip(1);
        reader.setResource(new UrlResource(url));
        reader.setLineMapper(new DefaultLineMapper<ShaparakRecord>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setDelimiter("|");
                setNames(new String[]{"rowNumber",
                        "pspCode",
                        "acceptorCode",
                        "traceCode",
                        "localDate",
                        "localTime",
                        "receiveDate",
                        "IBAN",
                        "depositeDate",
                        "depositeType",
                        "depositeCircleNumber",
                        "terminalType",
                        "processType",
                        "cardType",
                        "amount",
                        "rrn",
                        "depositeFlag",
                        "terminalCode"
                });
            }});
            setFieldSetMapper(new ShaparakRecordFieldSetMapper());
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<ShaparakRecord, ShaparakRecord> shaparakProcessor() {
        return new ShaparakRecordItemProcessor();
    }

    @Bean(name = "shaparakFileWriter")
    public ItemWriter<ShaparakRecord> writer(DataSource dataSource) {

        JdbcBatchItemWriter<ShaparakRecord> writer = new JdbcBatchItemWriter<ShaparakRecord>();
        writer.setItemSqlParameterSourceProvider(new ItemSqlParameterSourceProvider<ShaparakRecord>() {
            @Override
            public SqlParameterSource createSqlParameterSource(ShaparakRecord item) {
                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
                mapSqlParameterSource.addValue("acceptorCode", item.getKey().getAcceptorCode());
                mapSqlParameterSource.addValue("id", item.getId());
                StringWriter stringWriter = new StringWriter();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    objectMapper.writeValue(stringWriter, item.getFields());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mapSqlParameterSource.addValue("jsonData", stringWriter.toString());
                mapSqlParameterSource.addValue("traceCode", item.getKey().getTraceCode());
                mapSqlParameterSource.addValue("localDateTime", item.getKey().getLocalDateTime());
                mapSqlParameterSource.addValue("terminalCode", item.getKey().getTerminalCode());
                mapSqlParameterSource.addValue("amount", item.getKey().getAmount());
                mapSqlParameterSource.addValue("rrn", item.getKey().getRrn());
                mapSqlParameterSource.addValue("pspCode", item.getPspCode());
                return mapSqlParameterSource;
            }
        });
        writer.setSql("insert ignore into shaparakData (id, acceptorCode, traceCode, localDateTime, amount, rrn, terminalCode, pspCode, jsonData)" +
                "values (:id, :acceptorCode, :traceCode, :localDateTime, :amount, :rrn, :terminalCode, :pspCode, :jsonData)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean(name = "shaparakFileJob")
    public Job importUserJob(JobBuilderFactory jobs, @Qualifier(value = "shaparakFileStep") Step s1) {
        return jobs.get("ShaparakFileImportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean(name = "shaparakFileStep")
    public Step step1(StepBuilderFactory stepBuilderFactory,
                      @Qualifier("shaparakFileReader") ItemReader<ShaparakRecord> reader,
                      @Qualifier("shaparakFileWriter") ItemWriter<ShaparakRecord> writer,
                      @Qualifier("shaparakDatabaseWriter") ItemWriter<ShaparakRecord> repositoryWriter,
                      ShaparakJobListener shaparakJobListener) {
        CompositeItemWriter<ShaparakRecord> writer1 = new CompositeItemWriter<ShaparakRecord>();
        writer1.setDelegates(Arrays.<ItemWriter<? super ShaparakRecord>>asList(writer, repositoryWriter));
        return stepBuilderFactory.get("shaparakFileStep")
                .<ShaparakRecord, ShaparakRecord>chunk(10)
                .reader(reader)
                .processor(shaparakProcessor())
                .writer(writer1)
                .listener((org.springframework.batch.core.StepExecutionListener) shaparakJobListener)
                .build();
    }
}
