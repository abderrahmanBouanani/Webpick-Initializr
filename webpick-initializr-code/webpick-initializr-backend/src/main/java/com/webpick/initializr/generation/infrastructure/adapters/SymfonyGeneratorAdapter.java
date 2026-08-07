package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.application.ports.out.IStackGeneratorStrategy;
import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.FrontendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class SymfonyGeneratorAdapter implements IStackGeneratorStrategy {

    private final FreeMarkerEngineAdapter templateEngine;

    public SymfonyGeneratorAdapter(FreeMarkerEngineAdapter templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean supports(BackendFramework frameworkType) {
        return frameworkType == BackendFramework.SYMFONY;
    }

    @Override
    public boolean supports(FrontendFramework frameworkType) {
        return frameworkType == FrontendFramework.NONE;
    }

    @Override
    public void generate(GenerationContext context, Path targetPath) {
        try {
            Path configDir = targetPath.resolve("config");
            Path publicDir = targetPath.resolve("public");

            Files.createDirectories(configDir);
            Files.createDirectories(publicDir);

            Map<String, Object> data = new HashMap<>();
            data.put("projectName", context.getProjectName());
            data.put("dependencies", context.getSelectedDependencies());
            data.put("database", context.getDatabaseType() != null ? context.getDatabaseType().name() : "NONE");

            // Composer.json
            byte[] composerJsonContent = templateEngine.render("symfony/composer.json.ftl", data).getBytes();
            Files.write(targetPath.resolve("composer.json"), composerJsonContent);

            // public/index.php
            byte[] indexPhpContent = templateEngine.render("symfony/index.php.ftl", data).getBytes();
            Files.write(publicDir.resolve("index.php"), indexPhpContent);

            // .env
            byte[] envContent = templateEngine.render("symfony/env.ftl", data).getBytes();
            Files.write(targetPath.resolve(".env"), envContent);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du projet Symfony", e);
        }
    }
}
