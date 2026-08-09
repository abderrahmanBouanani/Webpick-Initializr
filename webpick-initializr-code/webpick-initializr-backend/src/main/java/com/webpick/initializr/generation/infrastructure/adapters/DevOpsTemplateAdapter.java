package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.application.ports.out.IDevOpsGeneratorPort;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class DevOpsTemplateAdapter implements IDevOpsGeneratorPort {

    private final FreeMarkerEngineAdapter templateEngine;

    public DevOpsTemplateAdapter(FreeMarkerEngineAdapter templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void generatePipelines(GenerationContext context, Path targetPath) {
        try {
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("projectName", context.getProjectName());
            templateData.put("databaseType", context.getDatabaseType() != null ? context.getDatabaseType().name() : "");
            templateData.put("backendFramework", context.getBackendFramework() != null ? context.getBackendFramework().name() : "");
            templateData.put("buildTool", context.getMetadata("buildTool") != null ? context.getMetadata("buildTool") : "Maven");
            templateData.put("javaVersion", context.getMetadata("javaVersion") != null ? context.getMetadata("javaVersion") : "17");

            // Utilisation des méthodes métiers du domaine (Clean Architecture)
            if (context.isDockerEnabled()) {
                byte[] dockerContent = templateEngine.render("devops/docker-compose.yml.ftl", templateData).getBytes();
                Files.write(targetPath.resolve("docker-compose.yml"), dockerContent);

                String dockerfileTemplate = switch (context.getBackendFramework()) {
                    case SPRING -> "devops/Dockerfile_spring.ftl";
                    case EXPRESS -> "devops/Dockerfile_express.ftl";
                    case DJANGO -> "devops/Dockerfile_django.ftl";
                    case SYMFONY -> "devops/Dockerfile_symfony.ftl";
                };

                byte[] dockerfileContent = templateEngine.render(dockerfileTemplate, templateData).getBytes();
                Files.write(targetPath.resolve("Dockerfile"), dockerfileContent);
            }

            if (context.isJenkinsEnabled()) {
                byte[] jenkinsContent = templateEngine.render("devops/Jenkinsfile.ftl", templateData).getBytes();
                Files.write(targetPath.resolve("Jenkinsfile"), jenkinsContent);
            }

            if (context.isK8sEnabled()) {
                Path k8sDir = targetPath.resolve("k8s");
                Files.createDirectories(k8sDir);
                byte[] k8sContent = templateEngine.render("devops/k8s-deployment.yml.ftl", templateData).getBytes();
                Files.write(k8sDir.resolve("deployment.yml"), k8sContent);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération des fichiers DevOps", e);
        }
    }
}