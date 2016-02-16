package com.bamzy.insurance;

import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

/**
 * @author Bamdad
 */
@Configuration
public class IntegrationConfig {
    @Autowired
    JobLaunchingMessageHandler messageHandler;

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        final PeriodicTrigger trigger = new PeriodicTrigger(1000);
        final PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(trigger);
        return pollerMetadata;
    }

    @Bean
    public JobLaunchingMessageHandler integrationJobActivator(JobLauncher jobLauncher) {
        return new JobLaunchingMessageHandler(jobLauncher);
    }

    @ServiceActivator(inputChannel = "job-launcher")
    public void shaparakBatchActivator(JobLaunchRequest request) {
        try {
            messageHandler.launch(request);
        } catch (JobInstanceAlreadyCompleteException ignored) {
        } catch (JobExecutionException ignored) {
        }
    }
}
