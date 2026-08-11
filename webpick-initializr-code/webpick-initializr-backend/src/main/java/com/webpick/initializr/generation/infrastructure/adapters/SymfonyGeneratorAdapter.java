package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.application.ports.out.IStackGeneratorStrategy;
import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.DatabaseType;
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
            Path srcDir = targetPath.resolve("src");
            Path controllerDir = srcDir.resolve("Controller");

            Files.createDirectories(configDir);
            Files.createDirectories(publicDir);
            Files.createDirectories(srcDir);
            Files.createDirectories(controllerDir);

            // Compute PHP Namespace from basePackage (e.g. com.example.demo -> Com\Example\Demo)
            String basePackage = context.getBasePackage();
            String namespacePhp = "App";
            if (basePackage != null && !basePackage.trim().isEmpty()) {
                String[] parts = basePackage.split("\\.");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < parts.length; i++) {
                    String part = parts[i];
                    if (!part.isEmpty()) {
                        sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
                        if (i < parts.length - 1) {
                            sb.append("\\");
                        }
                    }
                }
                if (sb.length() > 0) {
                    namespacePhp = sb.toString();
                }
            }
            String namespaceJson = namespacePhp.replace("\\", "\\\\");

            Map<String, Object> data = new HashMap<>();
            data.put("projectName", context.getProjectName());
            data.put("dependencies", context.getSelectedDependencies());
            data.put("database", context.getDatabaseType() != null ? context.getDatabaseType().name() : "NONE");
            data.put("namespace", namespacePhp);
            data.put("namespaceJson", namespaceJson);

            // Composer.json
            byte[] composerJsonContent = templateEngine.render("symfony/composer.json.ftl", data).getBytes();
            Files.write(targetPath.resolve("composer.json"), composerJsonContent);

            // public/index.php
            byte[] indexPhpContent = templateEngine.render("symfony/index.php.ftl", data).getBytes();
            Files.write(publicDir.resolve("index.php"), indexPhpContent);

            // .env
            byte[] envContent = templateEngine.render("symfony/env.ftl", data).getBytes();
            Files.write(targetPath.resolve(".env"), envContent);

            // src/Kernel.php
            byte[] kernelContent = templateEngine.render("symfony/Kernel.php.ftl", data).getBytes();
            Files.write(srcDir.resolve("Kernel.php"), kernelContent);

            // src/Controller/HomeController.php
            byte[] homeControllerContent = templateEngine.render("symfony/HomeController.php.ftl", data).getBytes();
            Files.write(controllerDir.resolve("HomeController.php"), homeControllerContent);

            // config/bundles.php
            byte[] bundlesContent = templateEngine.render("symfony/bundles.php.ftl", data).getBytes();
            Files.write(configDir.resolve("bundles.php"), bundlesContent);

            // config/routes.yaml
            byte[] routesContent = templateEngine.render("symfony/routes.yaml.ftl", data).getBytes();
            Files.write(configDir.resolve("routes.yaml"), routesContent);

            // config/packages/doctrine.yaml (if database is POSTGRES or MYSQL)
            if (context.getDatabaseType() != null && 
                (context.getDatabaseType() == DatabaseType.POSTGRES || 
                 context.getDatabaseType() == DatabaseType.MYSQL)) {
                Path packagesDir = configDir.resolve("packages");
                Files.createDirectories(packagesDir);
                byte[] doctrineContent = templateEngine.render("symfony/doctrine.yaml.ftl", data).getBytes();
                Files.write(packagesDir.resolve("doctrine.yaml"), doctrineContent);
            }

            // Fichier .gitignore
            byte[] gitignoreContent = templateEngine.render("symfony/gitignore.ftl", data).getBytes();
            Files.write(targetPath.resolve(".gitignore"), gitignoreContent);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du projet Symfony", e);
        }
    }
}
