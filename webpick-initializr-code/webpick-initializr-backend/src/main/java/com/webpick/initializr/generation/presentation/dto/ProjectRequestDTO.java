package com.webpick.initializr.generation.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

@Schema(description = "Project generation request payload")
public class ProjectRequestDTO {

    @Schema(description = "Name of the generated project", example = "my-spring-app")
    private String projectName;

    @Schema(description = "Base package name or namespace", example = "com.example.demo")
    private String basePackage;

    @Schema(description = "Flag indicating whether to push to remote Git repository", example = "false")
    private boolean includeGit;

    @Schema(description = "GitHub Personal Access Token (required if includeGit is true)", example = "ghp_xxxxxxxxxxxx")
    private String gitToken;

    @Schema(description = "Backend framework (spring, express, django, symfony)", example = "spring")
    private String backend;

    @Schema(description = "Frontend framework (angular, react, vue, none)", example = "angular")
    private String frontend;

    @Schema(description = "Database configuration (postgres, mysql, h2, mongodb)", example = "postgres")
    private String db;

    @Schema(description = "List of framework dependencies to bundle", example = "[\"jpa\", \"security\", \"lombok\"]")
    private List<String> deps;

    @Schema(description = "List of DevOps configuration templates to include (docker, docker-compose, jenkins, kubernetes)", example = "[\"docker\", \"docker-compose\"]")
    private List<String> devops;

    @Schema(description = "Additional key-value metadata for template customizations", example = "{\"javaVersion\": \"17\"}")
    private Map<String, String> metadata;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public boolean isIncludeGit() {
        return includeGit;
    }

    public void setIncludeGit(boolean includeGit) {
        this.includeGit = includeGit;
    }

    public String getGitToken() {
        return gitToken;
    }

    public void setGitToken(String gitToken) {
        this.gitToken = gitToken;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public String getFrontend() {
        return frontend;
    }

    public void setFrontend(String frontend) {
        this.frontend = frontend;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public List<String> getDeps() {
        return deps;
    }

    public void setDeps(List<String> deps) {
        this.deps = deps;
    }

    public List<String> getDevops() {
        return devops;
    }

    public void setDevops(List<String> devops) {
        this.devops = devops;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
