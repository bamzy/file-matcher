package com.bamzy.insurance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.messaging.MessageChannel;

/**
 * Integration configuration for stuff needed to run after complete application start
 *
 * @author Bamdad
 */
@Configuration
public class ApplicationStartConfig {


    @Bean
    public MessageChannel contextRefreshed() {
        return new DirectChannel();
    }

    /**
     * when {@link org.springframework.context.event.ContextRefreshedEvent contextRefreshedEvent} occurs this bean creates
     * message on {@link #contextRefreshed()} channel
     *
     * @return message producer
     */
    @Bean
    public ApplicationEventListeningMessageProducer contextRefreshedMessageSource() {
        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
        producer.setOutputChannel(contextRefreshed());
        producer.setEventTypes(ContextRefreshedEvent.class);
        return producer;
    }

    /**
     * start totan and shaparak file adapter manually after complete application start
     *
     * @param contextRefreshedEvent
     */
    @ServiceActivator(inputChannel = "contextRefreshed")
    public void started(ContextRefreshedEvent contextRefreshedEvent) {
        final ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        final SourcePollingChannelAdapter totanAdapter = (SourcePollingChannelAdapter) applicationContext.
                getBean("totanIntegrationConfig.totanMessageSource.inboundChannelAdapter");
        final SourcePollingChannelAdapter shaparakAdapter = (SourcePollingChannelAdapter) applicationContext.
                getBean("shaparakIntegrationConfig.shaparakMessageSource.inboundChannelAdapter");
        totanAdapter.start();
        shaparakAdapter.start();
    }
}
