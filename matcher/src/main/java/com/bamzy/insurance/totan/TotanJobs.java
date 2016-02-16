package com.bamzy.insurance.totan;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.annotation.Transformer;

import java.io.File;
import java.util.Date;

/**
 * @author Bamdad
 */
@Configuration
public class TotanJobs {
    private Job fileJob;
    private Job databaseJob;

    @Autowired
    @Qualifier("totanFileJob")
    public void setFileJob(Job fileJob) {
        this.fileJob = fileJob;
    }

    @Autowired
    @Qualifier("totanDatabaseJob")
    public void setDatabaseJob(Job databaseJob) {
        this.databaseJob = databaseJob;
    }

    @Transformer(inputChannel = "totan", outputChannel = "job-launcher")
    public JobLaunchRequest toRequest(File file) {
        final JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString("input.file.name", file.getAbsolutePath());
        return new JobLaunchRequest(fileJob, parametersBuilder.toJobParameters());
    }

    @Transformer(inputChannel = "totanDatabase", outputChannel = "job-launcher")
    public JobLaunchRequest totanDatabaseRead(ContextRefreshedEvent event) {

        return new JobLaunchRequest(databaseJob, new JobParametersBuilder().addDate("start.data", new Date()).toJobParameters());
    }
}
