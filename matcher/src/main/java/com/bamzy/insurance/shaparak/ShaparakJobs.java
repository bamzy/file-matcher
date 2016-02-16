package com.bamzy.insurance.shaparak;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;
import java.util.Date;

/**
 * @author Bamdad
 */
@Configuration
public class ShaparakJobs {

    private Job fileJob;
    private Job databaseJob;

    @Autowired
    @Qualifier("shaparakDatabaseJob")
    public void setDatabaseJob(Job job) {
        this.databaseJob = job;
    }

    @Autowired
    @Qualifier("shaparakFileJob")
    public void setJob(Job job) {
        this.fileJob = job;
    }

    @Transformer(inputChannel = "shaparak", outputChannel = "job-launcher")
    public JobLaunchRequest toRequest(Message<File> message) {
        final JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString("input.file.name", message.getPayload().getAbsolutePath());
        return new JobLaunchRequest(fileJob, parametersBuilder.toJobParameters());
    }

    @Transformer(inputChannel = "contextRefreshed", outputChannel = "job-launcher")
    public JobLaunchRequest shaparakDatabaseRead(ContextRefreshedEvent event) {

        return new JobLaunchRequest(databaseJob, new JobParametersBuilder().addDate("start.data", new Date()).toJobParameters());
    }

}
