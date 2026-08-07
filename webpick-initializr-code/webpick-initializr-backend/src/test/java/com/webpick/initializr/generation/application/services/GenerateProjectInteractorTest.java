package com.webpick.initializr.generation.application.services;

import com.webpick.initializr.generation.application.events.ProjectGeneratedEvent;
import com.webpick.initializr.generation.application.ports.out.IArchivePort;
import com.webpick.initializr.generation.application.ports.out.IConfigRepositoryPort;
import com.webpick.initializr.generation.application.ports.out.IDevOpsGeneratorPort;
import com.webpick.initializr.generation.application.ports.out.IStackGeneratorStrategy;
import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.FrontendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GenerateProjectInteractorTest {

    private IStackGeneratorStrategy strategy;
    private IDevOpsGeneratorPort devOpsGeneratorPort;
    private IArchivePort archivePort;
    private IConfigRepositoryPort repoPort;
    private ApplicationEventPublisher eventPublisher;
    private GenerateProjectInteractor interactor;

    @BeforeEach
    void setUp() {
        strategy = mock(IStackGeneratorStrategy.class);
        devOpsGeneratorPort = mock(IDevOpsGeneratorPort.class);
        archivePort = mock(IArchivePort.class);
        repoPort = mock(IConfigRepositoryPort.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        interactor = new GenerateProjectInteractor(List.of(strategy), devOpsGeneratorPort, archivePort, repoPort, eventPublisher);
    }

    @Test
    void testExecuteSuccessWithoutGit() {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("test-project")
                .basePackage("com.test")
                .backendFramework(BackendFramework.SPRING)
                .frontendFramework(FrontendFramework.NONE)
                .includeGit(false)
                .build();

        byte[] expectedZip = "zip-content".getBytes();

        when(strategy.supports(BackendFramework.SPRING)).thenReturn(true);
        when(strategy.supports(FrontendFramework.NONE)).thenReturn(true);
        when(archivePort.compressToZip(any(Path.class))).thenReturn(expectedZip);

        byte[] result = interactor.execute(context);

        assertArrayEquals(expectedZip, result);
        verify(strategy, times(1)).generate(eq(context), any(Path.class));
        verify(devOpsGeneratorPort, times(1)).generatePipelines(eq(context), any(Path.class));
        verify(repoPort, times(1)).saveHistory(context);
        verify(eventPublisher, never()).publishEvent(any(ProjectGeneratedEvent.class));
    }

    @Test
    void testExecuteSuccessWithGit() {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("test-project")
                .basePackage("com.test")
                .backendFramework(BackendFramework.SPRING)
                .frontendFramework(FrontendFramework.NONE)
                .includeGit(true)
                .gitToken("dummy-token")
                .build();

        byte[] expectedZip = "zip-content".getBytes();

        when(strategy.supports(BackendFramework.SPRING)).thenReturn(true);
        when(strategy.supports(FrontendFramework.NONE)).thenReturn(true);
        when(archivePort.compressToZip(any(Path.class))).thenReturn(expectedZip);

        byte[] result = interactor.execute(context);

        assertArrayEquals(expectedZip, result);
        verify(eventPublisher, times(1)).publishEvent(any(ProjectGeneratedEvent.class));
    }

    @Test
    void testExecuteNoStrategyFound() {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("test-project")
                .basePackage("com.test")
                .backendFramework(BackendFramework.DJANGO)
                .frontendFramework(FrontendFramework.NONE)
                .includeGit(false)
                .build();

        when(strategy.supports(any(BackendFramework.class))).thenReturn(false);

        assertThrows(RuntimeException.class, () -> interactor.execute(context));
    }
}
