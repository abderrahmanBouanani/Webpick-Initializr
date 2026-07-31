package com.webpick.initializr.generation.presentation.dto;

import java.util.List;
import java.util.Map;

public class ProjectRequestDTO {
    private String projectName;
    private String basePackage;
    private boolean includeGit;
    private String gitToken;
    private String backend;
    private String frontend;
    private String db;
    private List<String> deps;
    private List<String> devops;
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
