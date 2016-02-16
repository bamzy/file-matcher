package com.bamzy.insurance;

import com.bamzy.insurance.fanap.FanapPaymentService;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Bamdad
 */
@SpringBootApplication
@EnableScheduling
@EnableIntegration
@EnableBatchProcessing
@IntegrationComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public StepScope stepScope() {
        return new StepScope();
    }
    @Bean
    public FanapPaymentService fanapPaymentService(){
        return new FanapPaymentService();
    }
}
