package com.webpick.initializr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // Active le traitement asynchrone dans tout le Modulith
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Nombre de threads minimum actifs
        executor.setMaxPoolSize(5);  // Nombre maximum de requêtes Git simultanées
        executor.setQueueCapacity(50); // File d'attente si GitHub répond lentement
        executor.setThreadNamePrefix("GitIntegration-");
        executor.initialize();
        return executor;
    }
}