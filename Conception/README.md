# Architecture Logicielle et Conception

Ce dossier contient les détails de la conception et de l'architecture logicielle du projet **Webpick Initializr**.

L'application est conçue en suivant les principes de l'**Architecture Hexagonale (Ports & Adaptateurs)**, ce qui permet d'isoler le cœur de métier (le domaine) des détails techniques et des frameworks (la base de données PostgreSQL, le moteur de template FreeMarker, les APIs externes comme GitHub, et l'API REST Spring Boot).

---

## Structure du Projet

L'arborescence des packages Java respecte strictement cette séparation des responsabilités :

```text
src/main/java/com/webpick/initializr
│
├── generation/                     (MODULE 1 : Cœur de métier du projet)
│   │
│   ├── domain/                     (Couche 1 : Le Noyau)
│   │   ├── GenerationContext.java  (Données pures du projet à générer)
│   │   └── ProjectException.java   (Gestion des erreurs métier)
│   │
│   ├── application/                (Couche 2 : L'Orchestrateur)
│   │   ├── ports/                  (Les "Prises électriques" / Contrats)
│   │   │   ├── in/                 
│   │   │   │   └── IGenerateProjectUseCase.java (Port d'entrée)
│   │   │   └── out/                
│   │   │       ├── ITemplateEnginePort.java     (Besoin de génération de template)
│   │   │       ├── IArchivePort.java            (Besoin d'archivage zip)
│   │   │       └── IConfigRepositoryPort.java   (Besoin de persistance BDD)
│   │   │
│   │   ├── GenerateProjectInteractor.java      (L'interacteur / Cas d'utilisation)
│   │   └── ProjectGeneratedEvent.java          (Événement publié après génération)
│   │
│   ├── infrastructure/             (Couche 3 : Adaptateurs techniques)
│   │   ├── FreeMarkerEngineAdapter.java        (Implémentation de ITemplateEnginePort)
│   │   ├── ZipArchiveAdapter.java              (Implémentation de IArchivePort)
│   │   └── PostgresConfigAdapter.java          (Implémentation de IConfigRepositoryPort)
│   │
│   └── presentation/               (Couche 4 : Exposition Web)
│       ├── InitializrController.java           (Contrôleur API REST)
│       ├── ProjectRequestDTO.java              (DTO de requête d'entrée)
│       └── ProjectMapper.java                  (Mapping DTO <=> Domaine)
│
└── git/                            (MODULE 2 : Intégration Git - Isolé)
    ├── application/
    │   └── GitPushEventListener.java           (Écouteur d'événement de génération)
    └── infrastructure/
        ├── IGitRemotePort.java                 (Interface d'abstraction Git)
        └── GithubApiAdapter.java               (Implémentation de l'API GitHub)
```

---

## Description des Modules

La conception est divisée en deux modules autonomes et faiblement couplés :

### [Module 1 : Génération (Cœur de Métier)](1-generation-module.md)
Ce module gère toute la logique métier de création d'un projet, de la validation des données d'entrée jusqu'à la compilation des templates et la compression finale en archive ZIP.
*   [Consulter la documentation détaillée du module de génération](1-generation-module.md)

### [Module 2 : Git (Intégration Isolée)](2-git-module.md)
Ce module s'occupe de l'intégration avec les dépôts distants (notamment GitHub). Il est asynchrone et réagit aux événements émis par le module de génération, ce qui évite de polluer le cœur de métier avec des problématiques d'APIs externes.
*   [Consulter la documentation détaillée du module Git](2-git-module.md)

---

## Diagrammes de Conception

Pour une meilleure visualisation, les diagrammes de conception associés sont disponibles dans les sous-dossiers dédiés :

*   **Diagrammes Logiques** (dans [logique/](logique/)) :
    *   Architecture globale du système : [arch.png](logique/arch.png) / [arch.drawio](logique/arch.drawio)
*   **Diagrammes Logiciels** (dans [logiciel/](logiciel/)) :
    *   Diagramme de classes : [class_diagram.png](logiciel/class_diagram.png)
    *   Diagramme de séquence : [sequence_diagram.png](logiciel/sequence_diagram.png)
    *   Diagramme de cas d'utilisation : [usecase_diagram.png](logiciel/usecase_diagram.png)
