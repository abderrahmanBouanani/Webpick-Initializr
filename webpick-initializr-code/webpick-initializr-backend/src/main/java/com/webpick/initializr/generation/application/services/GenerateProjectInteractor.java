package com.webpick.initializr.generation.application.services;

import com.webpick.initializr.generation.application.events.ProjectGeneratedEvent;
import com.webpick.initializr.generation.application.ports.in.IGenerateProjectUseCase;
import com.webpick.initializr.generation.application.ports.out.IArchivePort;
import com.webpick.initializr.generation.application.ports.out.IConfigRepositoryPort;
import com.webpick.initializr.generation.application.ports.out.IDevOpsGeneratorPort;
import com.webpick.initializr.generation.application.ports.out.IStackGeneratorStrategy;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.context.ApplicationEventPublisher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GenerateProjectInteractor implements IGenerateProjectUseCase {
    private List<IStackGeneratorStrategy> strategies;
    private IDevOpsGeneratorPort devOpsGeneratorPort;
    private IArchivePort archivePort;
    private IConfigRepositoryPort repoPort;
    private ApplicationEventPublisher eventPublisher;

    public GenerateProjectInteractor(List<IStackGeneratorStrategy> strategies,
                                     IDevOpsGeneratorPort devOpsGeneratorPort,
                                     IArchivePort archivePort, IConfigRepositoryPort repoPort,
                                     ApplicationEventPublisher eventPublisher) {
        this.strategies = strategies;
        this.devOpsGeneratorPort = devOpsGeneratorPort;
        this.archivePort = archivePort;
        this.repoPort = repoPort;
        this.eventPublisher = eventPublisher;
    }



    @Override
    public byte[] execute(GenerationContext context) {
        try {
            // 1 - creat a temporary directory for the project generation
            Path targetDirectory = Files.createTempDirectory("webpick-gen-" + context.getId().toString());

            // 2 - Find the appropriate strategy for the given backend and frontend frameworks
            IStackGeneratorStrategy strategy = strategies.stream()
                    .filter(s -> s.supports(context.getBackendFramework()) && s.supports(context.getFrontendFramework()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Aucune stratégie de génération trouvée pour le framework backend " + context.getBackendFramework() + " et le framework frontend " + context.getFrontendFramework()));

            // 3 - Generate the project structure
            strategy.generate(context, targetDirectory);

            // 4 - Generate DevOps files
            devOpsGeneratorPort.generatePipelines(context, targetDirectory);

            // 5 - Compress the generated project into a zip file
            byte[] zipContent = archivePort.compressToZip(targetDirectory);

            // 6 - Save the generation history
            repoPort.saveHistory(context);

            // 7 - Publish an event indicating that the project has been generated ( asychronously uncoupled)
            if(context.isIncludeGit() && context.hasValidGitCredentials()){
                ProjectGeneratedEvent event = new ProjectGeneratedEvent(context, zipContent);
                eventPublisher.publishEvent(event);
            }

            // (Optionnel) Nettoyage du répertoire temporaire sur le serveur
            FileUtils.deleteDirectory(targetDirectory.toFile());

            // 8. Retour de l'archive au contrôleur[cite: 1]
            return zipContent;

        } catch (Exception e) {
            throw new RuntimeException("Échec de la génération de l'écosystème du projet" + e.getMessage(), e);
        }
    }
}
