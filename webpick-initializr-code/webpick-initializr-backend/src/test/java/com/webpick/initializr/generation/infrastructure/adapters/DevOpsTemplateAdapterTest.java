package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.DatabaseType;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DevOpsTemplateAdapterTest {

    private DevOpsTemplateAdapter adapter;

    @BeforeEach
    void setUp() {
        FreeMarkerEngineAdapter engine = new FreeMarkerEngineAdapter();
        adapter = new DevOpsTemplateAdapter(engine);
    }

    @Test
    void testGeneratePipelinesAllEnabled(@TempDir Path tempDir) throws IOException {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("my-project")
                .basePackage("com.example")
                .backendFramework(BackendFramework.SPRING)
                .databaseType(DatabaseType.POSTGRES)
                .devopsTools(List.of("docker", "jenkins", "k8s"))
                .includeGit(false)
                .build();

        adapter.generatePipelines(context, tempDir);

        assertTrue(Files.exists(tempDir.resolve("docker-compose.yml")));
        assertTrue(Files.exists(tempDir.resolve("Dockerfile")));
        assertTrue(Files.exists(tempDir.resolve("Jenkinsfile")));
        assertTrue(Files.exists(tempDir.resolve("k8s/deployment.yml")));
    }
}
