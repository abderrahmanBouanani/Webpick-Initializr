package com.webpick.initializr.generation.presentation.mappers;

import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.DatabaseType;
import com.webpick.initializr.generation.domain.entities.FrontendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.generation.presentation.dto.ProjectRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public GenerationContext toGenerationContext(ProjectRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        GenerationContext.Builder builder = new GenerationContext.Builder();
        builder.projectName(dto.getProjectName());
        builder.basePackage(dto.getBasePackage());
        builder.includeGit(dto.isIncludeGit());
        builder.gitToken(dto.getGitToken());

        if (dto.getBackend() != null) {
            try {
                builder.backendFramework(BackendFramework.valueOf(dto.getBackend().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Ignore or handle
            }
        }

        if (dto.getFrontend() != null) {
            try {
                builder.frontendFramework(FrontendFramework.valueOf(dto.getFrontend().toUpperCase()));
            } catch (IllegalArgumentException e) {
                builder.frontendFramework(FrontendFramework.NONE);
            }
        } else {
            builder.frontendFramework(FrontendFramework.NONE);
        }

        if (dto.getDb() != null) {
            try {
                builder.databaseType(DatabaseType.valueOf(dto.getDb().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Ignore or handle
            }
        }

        builder.selectedDependencies(dto.getDeps());
        builder.devopsTools(dto.getDevops());
        builder.projectMetadata(dto.getMetadata());

        return builder.build();
    }
}
