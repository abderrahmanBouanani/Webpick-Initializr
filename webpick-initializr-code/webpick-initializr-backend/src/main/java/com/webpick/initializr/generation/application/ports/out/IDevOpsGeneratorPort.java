package com.webpick.initializr.generation.application.ports.out;

import com.webpick.initializr.generation.domain.entities.GenerationContext;

import java.nio.file.Path;

public interface IDevOpsGeneratorPort {
    void generatePipelines(GenerationContext context, Path targetPath);
}
