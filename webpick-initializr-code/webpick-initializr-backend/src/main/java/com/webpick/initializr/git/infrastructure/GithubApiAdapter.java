package com.webpick.initializr.git.infrastructure;

import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.git.out.IGitRemotePort;
import org.springframework.stereotype.Component;

@Component
public class GithubApiAdapter implements IGitRemotePort {

    @Override
    public void pushToRemote(GenerationContext context, byte[] zipContent) {
        String token = context.getGitToken();
        String projectName = context.getProjectName();

        // Appel concret à l'API GitHub via un client HTTP (ex: WebClient ou RestTemplate)
        // pour créer le dépôt et y pousser l'arborescence générée.
        System.out.println("Connexion à l'API GitHub pour le projet : " + projectName);
        // ... Logique technique d'appel REST GitHub ...
    }
}