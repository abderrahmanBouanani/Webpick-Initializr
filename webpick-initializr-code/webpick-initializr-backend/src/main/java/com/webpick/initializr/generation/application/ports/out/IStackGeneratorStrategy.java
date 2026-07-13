package com.webpick.initializr.generation.application.ports.out;

import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.FrontendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;

import java.nio.file.Path;

public interface IStackGeneratorStrategy {
    boolean supports(BackendFramework frameworkType);

    boolean supports(FrontendFramework frameworkType);

    void generate(GenerationContext context, Path targetPath);
}
