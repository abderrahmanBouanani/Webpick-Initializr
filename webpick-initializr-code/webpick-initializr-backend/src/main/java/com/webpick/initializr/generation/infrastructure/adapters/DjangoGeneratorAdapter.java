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
public class DjangoGeneratorAdapter implements IStackGeneratorStrategy {

    private final FreeMarkerEngineAdapter templateEngine;

    public DjangoGeneratorAdapter(FreeMarkerEngineAdapter templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean supports(BackendFramework frameworkType) {
        return frameworkType == BackendFramework.DJANGO;
    }

    @Override
    public boolean supports(FrontendFramework frameworkType) {
        return frameworkType == FrontendFramework.NONE;
    }

    @Override
    public void generate(GenerationContext context, Path targetPath) {
        try {
            String projectName = context.getProjectName().replace("-", "_");
            Path projectDir = targetPath.resolve(projectName);
            Files.createDirectories(projectDir);

            Map<String, Object> data = new HashMap<>();
            data.put("projectName", projectName);
            data.put("dependencies", context.getSelectedDependencies());
            data.put("database", context.getDatabaseType() != null ? context.getDatabaseType().name() : "SQLITE");

            // Fichier manage.py
            byte[] managePyContent = templateEngine.render("django/manage.py.ftl", data).getBytes();
            Files.write(targetPath.resolve("manage.py"), managePyContent);

            // Fichier settings.py
            byte[] settingsContent = templateEngine.render("django/settings.py.ftl", data).getBytes();
            Files.write(projectDir.resolve("settings.py"), settingsContent);

            // Fichier requirements.txt
            byte[] reqsContent = templateEngine.render("django/requirements.txt.ftl", data).getBytes();
            Files.write(targetPath.resolve("requirements.txt"), reqsContent);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du projet Django", e);
        }
    }
}
