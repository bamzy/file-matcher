package com.bamzy.insurance.shaparak;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * @author Bamdad
 */
@MessagingGateway(defaultRequestChannel = "job-listener-channel", defaultRequestTimeout = 5000, defaultReplyTimeout = 5000)
public interface ShaparakJobListener extends StepExecutionListener, ChunkListener {
}
