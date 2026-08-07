package com.webpick.initializr.git.application;

import com.webpick.initializr.generation.application.events.ProjectGeneratedEvent;
import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.git.out.IGitRemotePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class GitPushEventListenerTest {

    private IGitRemotePort gitPort;
    private GitPushEventListener listener;

    @BeforeEach
    void setUp() {
        gitPort = mock(IGitRemotePort.class);
        listener = new GitPushEventListener(gitPort);
    }

    @Test
    void testOnProjectGeneratedWithGitEnabled() {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("test-proj")
                .basePackage("com.test")
                .backendFramework(BackendFramework.SPRING)
                .includeGit(true)
                .gitToken("dummy-token")
                .build();

        byte[] zipBytes = "zip-data".getBytes();
        ProjectGeneratedEvent event = new ProjectGeneratedEvent(context, zipBytes);

        listener.onProjectGenerated(event);

        verify(gitPort, times(1)).pushToRemote(context, zipBytes);
    }

    @Test
    void testOnProjectGeneratedWithGitDisabled() {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("test-proj")
                .basePackage("com.test")
                .backendFramework(BackendFramework.SPRING)
                .includeGit(false)
                .build();

        byte[] zipBytes = "zip-data".getBytes();
        ProjectGeneratedEvent event = new ProjectGeneratedEvent(context, zipBytes);

        listener.onProjectGenerated(event);

        verify(gitPort, never()).pushToRemote(any(GenerationContext.class), any(byte[].class));
    }
}
