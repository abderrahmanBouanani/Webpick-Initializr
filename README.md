# Webpick Initializr

Webpick Initializr is a specialized tool designed to automate the bootstrapping and generation of custom project configurations. It provides a web-based interface and backend infrastructure to generate project structures for multiple backend technologies, package them, and push them to remote repositories.

## Architecture

The project is built following the principles of Hexagonal Architecture (Ports and Adapters), ensuring a strict decoupling between domain logic and infrastructure concerns:

- **Domain Layer**: Contains core business entities and logic (e.g., framework types, generation context, technology models).
- **Application Layer**: Defines use cases (e.g., project generation) and interfaces (ports) for external communications.
- **Infrastructure Layer**: Implements adapters for concrete technologies (e.g., FreeMarker engine, Zip archiver, Spring/Django/Express generation strategies, database persistence).
- **Presentation Layer**: Handles incoming HTTP API requests and maps DTOs to internal context formats.

## Key Modules

### Generation Module
Responsible for rendering code templates and assembling directory structures.
- Strategies for Spring Boot, Django, and Express.
- Template rendering driven by FreeMarker.
- DevOps templates generation (Docker, Jenkins, Kubernetes).
- Project packaging into ZIP archives.

### Git Module
Handles communication with remote Git servers (like GitHub) to automatically push generated projects.
- Defined boundaries via ports.
- Asynchronous push event processing.
