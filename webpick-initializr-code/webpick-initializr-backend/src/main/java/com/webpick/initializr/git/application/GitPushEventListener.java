package com.webpick.initializr.git.application;

import com.webpick.initializr.generation.application.events.ProjectGeneratedEvent;
import com.webpick.initializr.git.out.IGitRemotePort;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GitPushEventListener {

    private final IGitRemotePort gitPort;

    public GitPushEventListener(IGitRemotePort gitPort) {
        this.gitPort = gitPort;
    }

    @Async // Crucial pour l'isolation asynchrone exigée par l'architecture
    @EventListener
    public void onProjectGenerated(ProjectGeneratedEvent event) {
        // On vérifie que l'utilisateur a bien demandé l'intégration Git
        if (event.getContext().isIncludeGit() && event.getContext().hasValidGitCredentials()) {
            try {
                System.out.println("Track 2 (Asynchrone) : Début du push vers GitHub...");
                gitPort.pushToRemote(event.getContext(), event.getZipContent());
                System.out.println("Track 2 (Asynchrone) : Push terminé avec succès.");
            } catch (Exception e) {
                // Tolérance aux pannes : l'erreur est journalisée, le client a déjà reçu son ZIP
                System.err.println("Échec de l'intégration Git en arrière-plan : " + e.getMessage());
            }
        }
    }
}