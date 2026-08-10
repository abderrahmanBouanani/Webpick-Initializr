package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.application.ports.out.IStackGeneratorStrategy;
import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.FrontendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class SpringGeneratorAdapter implements IStackGeneratorStrategy {
    private final FreeMarkerEngineAdapter templateEngine;

    public SpringGeneratorAdapter(FreeMarkerEngineAdapter templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public boolean supports(BackendFramework frameworkType) {
        return frameworkType == BackendFramework.SPRING;
    }

    @Override
    public boolean supports(FrontendFramework frameworkType) {
        return frameworkType == FrontendFramework.NONE;
    }

    @Override
    public void generate(GenerationContext context, Path targetPath) {
        try{
            // 1. Création de l'arborescence standard Maven/Gradle
            String packagePath = context.getBasePackage().replace(".", "/");
            Path srcMainJava = targetPath.resolve("src/main/java/" + packagePath);
            Path srcMainResources = targetPath.resolve("src/main/resources");
            Path srcTestJava = targetPath.resolve("src/test/java/" + packagePath);

            Files.createDirectories(srcMainJava);
            Files.createDirectories(srcMainResources);
            Files.createDirectories(srcTestJava);

            // Préparation des données pour le moteur de template (FreeMarker)
            Map<String, Object> templateData = prepareTemplateData(context);

            // 2. Génération de la classe principale (Application.java)
            byte[] mainClassContent = templateEngine.render("spring/Application.java.ftl", templateData).getBytes();
            Files.write(srcMainJava.resolve("Application.java"), mainClassContent);

            // 3. Génération du fichier de configuration (application.yml)
            byte[] appYmlContent = templateEngine.render("spring/application.yml.ftl", templateData).getBytes();
            Files.write(srcMainResources.resolve("application.yml"), appYmlContent);

            // 4. Logique conditionnelle de l'outil de build (Maven vs Gradle)
            String buildTool = context.getMetadata("buildTool"); // ex: "Maven" ou "Gradle"

            if ("Gradle".equalsIgnoreCase(buildTool)) {
                byte[] buildContent = templateEngine.render("spring/build.gradle.ftl", templateData).getBytes();
                Files.write(targetPath.resolve("build.gradle"), buildContent);
                // Copie du Gradle Wrapper
                copyResource("/templates/spring/wrappers/gradle/gradlew", targetPath.resolve("gradlew"));
                copyResource("/templates/spring/wrappers/gradle/gradlew.bat", targetPath.resolve("gradlew.bat"));
                copyResource("/templates/spring/wrappers/gradle/gradle/wrapper/gradle-wrapper.jar", targetPath.resolve("gradle/wrapper/gradle-wrapper.jar"));
                copyResource("/templates/spring/wrappers/gradle/gradle/wrapper/gradle-wrapper.properties", targetPath.resolve("gradle/wrapper/gradle-wrapper.properties"));
            } else { // Maven par défaut
                byte[] buildContent = templateEngine.render("spring/pom.xml.ftl", templateData).getBytes();
                Files.write(targetPath.resolve("pom.xml"), buildContent);
                // Copie du Maven Wrapper
                copyResource("/templates/spring/wrappers/maven/mvnw", targetPath.resolve("mvnw"));
                copyResource("/templates/spring/wrappers/maven/mvnw.cmd", targetPath.resolve("mvnw.cmd"));
                copyResource("/templates/spring/wrappers/maven/.mvn/wrapper/maven-wrapper.properties", targetPath.resolve(".mvn/wrapper/maven-wrapper.properties"));
            }

            // 5. Génération du fichier .gitignore
            byte[] gitignoreContent = templateEngine.render("spring/gitignore.ftl", templateData).getBytes();
            Files.write(targetPath.resolve(".gitignore"), gitignoreContent);
        } catch (IOException e) {
            throw new RuntimeException("Erreur critique d'E/S lors de la génération du projet Spring Boot", e);
        }
    }

    private void copyResource(String resourcePath, Path targetFile) {
        try (var is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Ressource introuvable dans le classpath: " + resourcePath);
            }
            Files.createDirectories(targetFile.getParent());
            Files.copy(is, targetFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la copie de la ressource " + resourcePath + " vers " + targetFile, e);
        }
    }

    // Méthode utilitaire pour mapper le contexte pur en Map exploitable par FreeMarker
    private Map<String, Object> prepareTemplateData(GenerationContext context) {
        Map<String, Object> data = new HashMap<>();
        data.put("basePackage", context.getBasePackage());
        data.put("projectName", context.getProjectName());
        data.put("dependencies", context.getSelectedDependencies()); // Injectera JPA, Lombok, etc.
        data.put("javaVersion", context.getMetadata("javaVersion")); // ex: "21"
        data.put("groupId", context.getMetadata("groupId")); // ex: "com.webpick"
        data.put("artifactId", context.getMetadata("artifactId")); // ex: "core-service"
        data.put("database", context.getDatabaseType() != null ? context.getDatabaseType().name() : "POSTGRES");
        return data;
    }
}
