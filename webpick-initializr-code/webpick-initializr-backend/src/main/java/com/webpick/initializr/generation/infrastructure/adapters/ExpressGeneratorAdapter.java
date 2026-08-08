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
public class ExpressGeneratorAdapter implements IStackGeneratorStrategy {

    private final FreeMarkerEngineAdapter templateEngine;

    public ExpressGeneratorAdapter(FreeMarkerEngineAdapter templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean supports(BackendFramework frameworkType) {
        return frameworkType == BackendFramework.EXPRESS;
    }

    @Override
    public boolean supports(FrontendFramework frameworkType) {
        return frameworkType == FrontendFramework.NONE;
    }

    @Override
    public void generate(GenerationContext context, Path targetPath) {
        try {
            Path srcDir = targetPath.resolve("src");
            Files.createDirectories(srcDir);

            Map<String, Object> data = new HashMap<>();
            data.put("projectName", context.getProjectName());
            data.put("dependencies", context.getSelectedDependencies());
            data.put("database", context.getDatabaseType() != null ? context.getDatabaseType().name() : "NONE");

            // Fichier package.json
            byte[] packageJsonContent = templateEngine.render("express/package.json.ftl", data).getBytes();
            Files.write(targetPath.resolve("package.json"), packageJsonContent);

            // Fichier index.js ou index.ts
            byte[] indexContent = templateEngine.render("express/index.js.ftl", data).getBytes();
            Files.write(srcDir.resolve("index.js"), indexContent);

            // Fichier .gitignore
            byte[] gitignoreContent = templateEngine.render("express/gitignore.ftl", data).getBytes();
            Files.write(targetPath.resolve(".gitignore"), gitignoreContent);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du projet Express", e);
        }
    }
}
