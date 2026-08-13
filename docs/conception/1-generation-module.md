# Module 1 : Génération (Cœur de Métier)

Ce module constitue le noyau de l'application **Webpick Initializr**. Il est responsable de la réception de la demande utilisateur, de la validation des paramètres, de la compilation du code source et de la production d'une archive compressée.

Il est structuré en 4 couches distinctes selon les principes de l'architecture hexagonale.

---

## 1. Couche Présentation (Web)

Située dans le package `presentation`, cette couche sert de point d'entrée pour les requêtes HTTP. Elle s'occupe de la communication avec l'extérieur (le client).

*   **`InitializrController`** :
    Le contrôleur REST qui expose les points de terminaison (endpoints) de l'API. Il reçoit les requêtes de génération de projets, valide les aspects HTTP, délègue le traitement métier au cas d'utilisation correspondant (`IGenerateProjectUseCase`), et retourne le fichier ZIP résultant.
*   **`ProjectRequestDTO`** :
    Objet de transfert de données (Data Transfer Object) qui modélise et structure la requête HTTP reçue. Il inclut les annotations de validation (comme `@NotNull`, `@Size`) pour valider syntaxiquement les entrées avant qu'elles ne pénètrent le système.
*   **`ProjectMapper`** :
    Composant de mapping (généralement implémenté via MapStruct) servant de traducteur à la frontière de l'hexagone. Il convertit le `ProjectRequestDTO` (format web) en `GenerationContext` (format métier) afin de protéger le domaine interne des structures de la couche Web.

---

## 2. Couche Domaine (Noyau)

Située dans le package `domain`, c'est le cœur de l'hexagone. Elle contient les règles de gestion pures et est totalement indépendante des frameworks, des bases de données et des protocoles de communication.

*   **`GenerationContext`** :
    L'entité métier centrale de la génération. Elle encapsule toutes les données requises pour générer un projet (nom du projet, package racine, dépendances, jetons, etc.) et contient les règles et validations de cohérence strictement métier.
*   **`ProjectException`** :
    Classe d'exception propre au domaine métier. Elle permet de remonter des erreurs logiques ou des violations des règles métier de manière unifiée, sans couplage avec les exceptions de Spring ou d'autres librairies tierces.

---

## 3. Couche Application (Orchestration & Contrats)

Située dans le package `application`, cette couche orchestre les flux de contrôle et contient la logique des cas d'utilisation. Elle définit également les contrats (ports) qui lient le domaine aux adaptateurs externes.

### Ports d'Entrée (in)
*   **`IGenerateProjectUseCase` (Interface)** :
    Le port d'entrée principal du module. Il expose le contrat décrivant l'action de générer un projet. La couche de présentation communique uniquement avec cette interface, ce qui garantit le découplage.

### Ports de Sortie (out)
*   **`ITemplateEnginePort` (Interface)** :
    Port de sortie qui abstrait le besoin de générer des fichiers textuels à partir de modèles. Le domaine l'utilise pour générer le code source sans savoir quelle technologie de templating (FreeMarker, Velocity, Thymeleaf) est employée.
*   **`IArchivePort` (Interface)** :
    Port de sortie définissant le contrat pour compresser un ensemble de fichiers/dossiers dans un format d'archive (typiquement ZIP), masquant l'implémentation bas niveau du système de fichiers ou des flux mémoire.
*   **`IConfigRepositoryPort` (Interface)** :
    Port de sortie pour la persistance des données. Il définit les méthodes nécessaires pour sauvegarder l'historique et la configuration des projets générés en base de données.

### Orchestration & Événements
*   **`GenerateProjectInteractor`** :
    Le cas d'utilisation concret implémentant `IGenerateProjectUseCase`. Il orchestre le processus de génération :
    1. Reçoit le `GenerationContext` du domaine.
    2. Fait appel à `ITemplateEnginePort` pour générer le code source.
    3. Fait appel à `IArchivePort` pour empaqueter le code dans un ZIP.
    4. Persiste l'historique via `IConfigRepositoryPort`.
    5. Publie un événement système via `ApplicationEventPublisher` pour notifier les autres modules.
*   **`ApplicationEventPublisher` (Interface)** :
    Composant (souvent fourni par le framework d'application tel que Spring) qui permet de publier des événements au sein du système.
*   **`ProjectGeneratedEvent`** :
    La charge utile (payload) représentant l'événement publié de manière synchrone ou asynchrone dès que l'archive du projet est générée avec succès. Elle contient les métadonnées requises par les modules abonnés (ex: chemin du ZIP, nom du dépôt, configuration Git).

---

## 4. Couche Infrastructure (Détails Techniques)

Située dans le package `infrastructure`, cette couche implémente les détails techniques et s'adapte aux ports de sortie définis par la couche application.

*   **`FreeMarkerEngineAdapter`** :
    Adaptateur implémentant le port `ITemplateEnginePort`. Il utilise le moteur de template Apache FreeMarker pour compiler les modèles de fichiers `.ftl` avec les données du `GenerationContext`.
*   **`ZipArchiveAdapter`** :
    Adaptateur implémentant le port `IArchivePort`. Il gère l'écriture et la compression physique des fichiers au format `.zip` en manipulant les flux Java (`java.util.zip`).
*   **`PostgresConfigAdapter`** :
    Adaptateur implémentant le port `IConfigRepositoryPort`. Il interagit avec la base de données PostgreSQL en utilisant Spring Data JPA / Hibernate pour sauvegarder les entités de configuration de projet.
