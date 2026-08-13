# Module 2 : Git (Intégration Isolée)

Ce module gère l'intégration avec les plateformes d'hébergement de code (notamment GitHub). Il est totalement indépendant du module de génération principal, ce qui évite de ralentir ou de bloquer le cœur de métier lors d'appels réseau vers des APIs tierces.

Il fonctionne sur un modèle réactif (événementiel) et est structuré selon les principes d'isolation de l'architecture hexagonale.

---

## 1. Couche Application (Événementiel)

Située dans le package `application`, elle contient le point d'entrée réactif du module Git.

*   **`GitPushEventListener`** :
    Ce composant est un écouteur d'événements (Event Listener) qui reste à l'écoute des publications de `ProjectGeneratedEvent` émis par le module de génération. Dès qu'un projet est généré avec succès, cet écouteur capture l'événement (souvent de manière asynchrone) et orchestre le processus de dépôt :
    1. Récupère les métadonnées de l'événement (emplacement du projet, jetons d'accès, etc.).
    2. Appelle le port de sortie Git (`IGitRemotePort`) pour pousser le code généré.

---

## 2. Couche Infrastructure (Détails Techniques)

Cette couche implémente les contrats définis pour interagir avec les APIs externes ou effectuer des opérations système de bas niveau (Git).

*   **`IGitRemotePort` (Interface)** :
    *Anciennement nommé temporairement `GithubApiAdapter` dans l'arborescence préliminaire.*
    Il s'agit du port de sortie abstrait qui définit l'interface pour interagir avec un service de gestion de version distant. Il définit des méthodes telles que `createRepository(...)` et `pushCode(...)`. Cette abstraction évite au module de dépendre directement de l'API spécifique de GitHub ou GitLab.
*   **`GithubApiAdapter` (Implémentation)** :
    L'implémentation concrète de `IGitRemotePort` pour GitHub. Cet adaptateur réalise les opérations suivantes :
    *   Authentification via OAuth2 avec le jeton fourni dans le contexte.
    *   Construction des requêtes HTTP et gestion des en-têtes requis par l'API REST/GraphQL de GitHub.
    *   Création du dépôt sur le compte utilisateur GitHub.
    *   Initialisation locale du dépôt Git temporaire, création du premier commit et envoi (push) des sources vers la branche distante.
