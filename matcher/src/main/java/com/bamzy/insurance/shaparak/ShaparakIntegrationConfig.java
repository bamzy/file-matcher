package com.bamzy.insurance.shaparak;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.messaging.MessageChannel;

import java.io.File;

/**
 * class for managing shaparak integration channels
 *
 * @author Bamdad
 */
@Configuration
public class ShaparakIntegrationConfig {

    @Bean
    public MessageChannel shaparak() {
        return new DirectChannel();
    }

    /**
     * channel adapter for watching a folder and creating shaparak file import messages
     *
     * @return channel adapter
     * @throws Exception
     */
    @Bean
    @InboundChannelAdapter(value = "shaparak", autoStartup = "false")
    public FileReadingMessageSource shaparakMessageSource() throws Exception {
        final FileReadingMessageSource fileSource = new FileReadingMessageSource();
        fileSource.setDirectory(new File("data"));
        return fileSource;
    }

}
