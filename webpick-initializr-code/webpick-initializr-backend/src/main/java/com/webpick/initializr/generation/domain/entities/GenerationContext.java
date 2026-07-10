package com.webpick.initializr.generation.domain.entities;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GenerationContext {

    private final UUID id;
    private final String projectName;
    private final String basePackage;
    private final boolean includeGit;
    private final String gitToken;
    private final BackendFramework backendFramework;
    private final FrontendFramework frontendFramework;
    private final DatabaseType databaseType;
    private final List<String> selectedDependencies;
    private final List<String> devopsTools;
    private final Map<String, String> projectMetadata;

    private GenerationContext(Builder builder) {
        validate(builder);

        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.projectName = builder.projectName;
        this.basePackage = builder.basePackage;
        this.includeGit = builder.includeGit;
        this.gitToken = builder.gitToken;
        this.backendFramework = builder.backendFramework;
        this.frontendFramework = builder.frontendFramework;
        this.databaseType = builder.databaseType;

        // Immutabilité défensive pour les collections
        this.selectedDependencies = builder.selectedDependencies != null ? List.copyOf(builder.selectedDependencies) : Collections.emptyList();
        this.devopsTools = builder.devopsTools != null ? List.copyOf(builder.devopsTools) : Collections.emptyList();
        this.projectMetadata = builder.projectMetadata != null ? Map.copyOf(builder.projectMetadata) : Collections.emptyMap();
    }

    private void validate(Builder builder) {
        if (builder.projectName == null || builder.projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du projet est obligatoire.");
        }
        if (builder.basePackage == null || !builder.basePackage.matches("^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$")) {
            throw new IllegalArgumentException("Le format du package de base est invalide.");
        }
        if (builder.backendFramework == null) {
            throw new IllegalArgumentException("Le framework backend est obligatoire.");
        }
        if (builder.includeGit && (builder.gitToken == null || builder.gitToken.trim().isEmpty())) {
            throw new IllegalArgumentException("Le token Git est requis si l'intégration Git est activée.");
        }
    }

    // Getters
    public UUID getId() { return id; }
    public String getProjectName() { return projectName; }
    public String getBasePackage() { return basePackage; }
    public boolean isIncludeGit() { return includeGit; }
    public String getGitToken() { return gitToken; }
    public BackendFramework getBackendFramework() { return backendFramework; }
    public FrontendFramework getFrontendFramework() { return frontendFramework; }
    public DatabaseType getDatabaseType() { return databaseType; }
    public List<String> getSelectedDependencies() { return selectedDependencies; }
    public List<String> getDevopsTools() { return devopsTools; }

    // Méthodes spécifiques du domaine (Ubiquitous Language)
    public boolean hasFrontend() {
        return this.frontendFramework != null && this.frontendFramework != FrontendFramework.NONE;
    }

    public boolean isDockerEnabled() {
        return this.devopsTools.contains("docker");
    }

    public boolean isJenkinsEnabled() {
        return this.devopsTools.contains("jenkins");
    }

    public boolean isK8sEnabled() {
        return this.devopsTools.contains("k8s");
    }

    public String getMetadata(String key) {
        return this.projectMetadata.get(key);
    }

    public boolean hasValidGitCredentials() {
        return this.includeGit && this.gitToken != null && !this.gitToken.trim().isEmpty();
    }


    // Builder Class
    public static class Builder {
        private UUID id;
        private String projectName;
        private String basePackage;
        private boolean includeGit;
        private String gitToken;
        private BackendFramework backendFramework;
        private FrontendFramework frontendFramework;
        private DatabaseType databaseType;
        private List<String> selectedDependencies;
        private List<String> devopsTools;
        private Map<String, String> projectMetadata;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder projectName(String projectName) { this.projectName = projectName; return this; }
        public Builder basePackage(String basePackage) { this.basePackage = basePackage; return this; }
        public Builder includeGit(boolean includeGit) { this.includeGit = includeGit; return this; }
        public Builder gitToken(String gitToken) { this.gitToken = gitToken; return this; }
        public Builder backendFramework(BackendFramework backendFramework) { this.backendFramework = backendFramework; return this; }
        public Builder frontendFramework(FrontendFramework frontendFramework) { this.frontendFramework = frontendFramework; return this; }
        public Builder databaseType(DatabaseType databaseType) { this.databaseType = databaseType; return this; }
        public Builder selectedDependencies(List<String> selectedDependencies) { this.selectedDependencies = selectedDependencies; return this; }
        public Builder devopsTools(List<String> devopsTools) { this.devopsTools = devopsTools; return this; }
        public Builder projectMetadata(Map<String, String> projectMetadata) { this.projectMetadata = projectMetadata; return this; }

        public GenerationContext build() {
            return new GenerationContext(this);
        }
    }
}