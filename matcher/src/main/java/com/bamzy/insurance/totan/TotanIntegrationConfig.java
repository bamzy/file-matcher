package com.bamzy.insurance.totan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.messaging.MessageChannel;

import java.io.File;

/**
 * @author Bamdad
 */
@Configuration
public class TotanIntegrationConfig {

    @Bean
    public MessageChannel totan() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel totanRequest() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel totanDatabase() {
        return new DirectChannel();
    }

    /**
     * channel adapter for watching a folder for importing totan records from files
     *
     * @return channel adapter
     * @throws Exception
     */
    @Bean
    @InboundChannelAdapter(value = "totan", autoStartup = "false")
    public FileReadingMessageSource totanMessageSource() throws Exception {
        final FileReadingMessageSource fileSource = new FileReadingMessageSource();
        fileSource.setDirectory(new File("totan-data"));
        return fileSource;
    }

    @Bean
    public ApplicationEventListeningMessageProducer totanDatabaseReadingMessageSource() {

        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
        producer.setOutputChannel(totanDatabase());
        producer.setEventTypes(ContextRefreshedEvent.class);
        return producer;
    }

}
