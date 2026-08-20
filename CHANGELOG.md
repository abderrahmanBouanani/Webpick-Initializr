# Changelog

All notable changes to Webpick Initializr will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-09-27

### Added

#### Core Generation Engine
- Hexagonal Architecture implementation strictly decoupling domain logic from infrastructure and presentation concerns.
- Apache FreeMarker template adapter for dynamic code generation.
- Spring Boot stack generator with configurable Maven dependencies (JPA, Security, Lombok, MapStruct) and Java 17+ baseline.
- Express.js generator with npm configuration, scripts, and modular entry points.
- Django generator with `manage.py`, project settings, WSGI configuration, and requirements.
- Symfony generator supporting PHP 8+, PSR-4 dynamic namespaces, Maker Bundle, Twig, doctrine mappings, and default controller routing.
- In-memory ZIP archive assembler packaging generated directory trees into single downloadable archives.

#### DevOps & Infrastructure
- DevOps template adapters generating tailored `Dockerfile`, `docker-compose.yml`, `Jenkinsfile`, and Kubernetes manifests.
- Multi-stage Docker containerization for the backend service using Eclipse Temurin 17 JRE with non-root security.
- Multi-stage Docker containerization for the Angular frontend using Nginx 1.27 Alpine with SPA routing and API reverse proxy.
- Root `docker-compose.yml` for unified single-command deployment (`docker compose up`).

#### Remote Git Synchronization
- GitHub REST API adapter for asynchronous repository creation and file commits via Personal Access Token (`gitToken`).
- Event-driven publisher/listener architecture decoupling the generation use case from remote network calls.

#### Web Interface & API Documentation
- Angular 18 frontend with interactive stack configuration, real-time CLI command generator, and virtual file tree preview.
- OpenAPI 3.0 / Swagger UI interactive documentation available at `/swagger-ui/index.html` and JSON spec at `/v3/api-docs`.
- Presentation layer controller annotations with `@Tag`, `@Operation`, `@ApiResponses`, and `@Schema`.

#### Quality, CI/CD & Governance
- GitHub Actions CI workflow (`.github/workflows/ci.yml`) automating compilation, unit/integration testing, JaCoCo coverage reporting, Checkstyle analysis, and SpotBugs inspection.
- GitHub Actions Release workflow (`.github/workflows/release.yml`) automating GitHub Releases and JAR asset attachments on semantic tags.
- Open source MIT License declared in root `LICENSE` and Maven POM.
- Community and governance guidelines: `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, `SECURITY.md`, and issue/PR templates.
- Technical documentation reorganized in `/docs` covering API specifications, system configuration, architectural diagrams, and project milestones.
