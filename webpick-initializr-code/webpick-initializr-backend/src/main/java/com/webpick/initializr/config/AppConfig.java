package com.webpick.initializr.config;

import com.webpick.initializr.generation.application.ports.in.IGenerateProjectUseCase;
import com.webpick.initializr.generation.application.ports.out.IArchivePort;
import com.webpick.initializr.generation.application.ports.out.IConfigRepositoryPort;
import com.webpick.initializr.generation.application.ports.out.IDevOpsGeneratorPort;
import com.webpick.initializr.generation.application.ports.out.IStackGeneratorStrategy;
import com.webpick.initializr.generation.application.services.GenerateProjectInteractor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public IGenerateProjectUseCase generateProjectUseCase(
            List<IStackGeneratorStrategy> strategies,
            IDevOpsGeneratorPort devOpsGeneratorPort,
            IArchivePort archivePort,
            IConfigRepositoryPort repoPort,
            ApplicationEventPublisher eventPublisher) {
        return new GenerateProjectInteractor(strategies, devOpsGeneratorPort, archivePort, repoPort, eventPublisher);
    }
}
