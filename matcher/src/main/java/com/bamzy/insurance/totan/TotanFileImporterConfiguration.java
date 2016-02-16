package com.bamzy.insurance.totan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bamzy.insurance.model.TotanRecord;
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
import org.springframework.batch.item.file.transform.RegexLineTokenizer;
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
import java.util.regex.Pattern;

/**
 * Created by jalil on 1/24/2015.
 */
@Configuration
public class TotanFileImporterConfiguration {
    @Bean(name = "totanFileReader")
    @Scope("step")
    public FlatFileItemReader<TotanRecord> reader(@Value("#{jobParameters['input.file.name']}") String filePath) throws MalformedURLException {
        URL url = new File(filePath).toURI().toURL();
        FlatFileItemReader<TotanRecord> reader = new FlatFileItemReader<TotanRecord>();
        reader.setResource(new UrlResource(url));
        reader.setLineMapper(new DefaultLineMapper<TotanRecord>() {{
            setLineTokenizer(new RegexLineTokenizer() {{
                setPattern(Pattern.compile("([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)[,|]([^,|]*)"));


                setNames(new String[]{"rowNumber",
                        "accountNumber",
                        "c",
                        "amount",
                        "traceCode",
                        "description",
                        "insuranceNumber",
                        "uniqueTransactionNumber",
                        "localDateTime",
                        "gregorianDateTime",
                        "rrn",
                        "acceptorCode",
                        "terminalCode"
                });
                setFieldSetMapper(new TotanRecordFieldSetMapper());
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<TotanRecord, TotanRecord> processor() {
        return new TotanRecordItemProcessor();
    }

    @Bean(name = "totanFileWriter")
    public ItemWriter<TotanRecord> writer(DataSource dataSource) {
        JdbcBatchItemWriter<TotanRecord> writer = new JdbcBatchItemWriter<TotanRecord>();
        writer.setItemSqlParameterSourceProvider(new ItemSqlParameterSourceProvider<TotanRecord>() {
            @Override
            public SqlParameterSource createSqlParameterSource(TotanRecord item) {
                MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
                mapSqlParameterSource.addValue("acceptorCode", item.getKey().getAcceptorCode());
                mapSqlParameterSource.addValue("id", item.getId());
                mapSqlParameterSource.addValue("traceCode", item.getKey().getTraceCode());
                mapSqlParameterSource.addValue("localDateTime", item.getKey().getLocalDateTime());
                mapSqlParameterSource.addValue("terminalCode", item.getKey().getTerminalCode());
                mapSqlParameterSource.addValue("amount", item.getKey().getAmount());
                StringWriter stringWriter = new StringWriter();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    objectMapper.writeValue(stringWriter, item.getFields());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mapSqlParameterSource.addValue("jsonData", stringWriter.toString());
                mapSqlParameterSource.addValue("rrn", item.getKey().getRrn());
                mapSqlParameterSource.addValue("accountNumber", item.getAccountNumber());
                mapSqlParameterSource.addValue("content", item.getContent());
                return mapSqlParameterSource;
            }
        });
        writer.setSql("insert ignore into totanData (id, acceptorCode, traceCode, localDateTime, amount,jsonData, rrn, terminalCode, accountNumber, content)" +
                "values (:id, :acceptorCode, :traceCode, :localDateTime, :amount,:jsonData, :rrn, :terminalCode, :accountNumber, :content)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean(name = "totanFileJob")
    public Job importUserJob(JobBuilderFactory jobs, @Qualifier(value = "totanFileStep") Step s1) {
        return jobs.get("TotanFileImportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean(name = "totanFileStep")
    public Step step1(StepBuilderFactory stepBuilderFactory,
                      @Qualifier("totanFileReader") ItemReader<TotanRecord> reader,
                      @Qualifier("totanFileWriter") ItemWriter<TotanRecord> writer,
                      @Qualifier("totanDatabaseWriter") ItemWriter<TotanRecord> repositoryWriter) {
        CompositeItemWriter<TotanRecord> writer1 = new CompositeItemWriter<TotanRecord>();
        writer1.setDelegates(Arrays.<ItemWriter<? super TotanRecord>>asList(writer, repositoryWriter));
        return stepBuilderFactory.get("totanFileStep")
                .<TotanRecord, TotanRecord>chunk(10)
                .reader(reader)
                .processor(processor())
                .writer(writer1)
                .build();
    }
}
