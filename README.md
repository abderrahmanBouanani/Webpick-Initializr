# Webpick Initializr

[![CI](https://img.shields.io/github/actions/workflow/status/abderrahmanBouanani/Webpick-Initializr/ci.yml?branch=main&label=CI&style=flat-square)](https://github.com/abderrahmanBouanani/Webpick-Initializr/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](https://opensource.org/licenses/MIT)
[![Coverage](https://img.shields.io/badge/Coverage-JaCoCo-brightgreen.svg?style=flat-square)](https://github.com/abderrahmanBouanani/Webpick-Initializr)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg?style=flat-square)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen.svg?style=flat-square)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-18-red.svg?style=flat-square)](https://angular.dev)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-green.svg?style=flat-square)](http://localhost:8081/swagger-ui/index.html)

Webpick Initializr is an open-source project generator and scaffolding platform designed to automate the bootstrapping of custom software applications. It provides a web-based interactive interface and a modular backend architecture to generate production-ready projects across multiple backend frameworks, database engines, and DevOps environments, packaging them into downloadable archives or pushing them directly to remote Git repositories.

---

## Architecture

The project is architected following the Hexagonal Architecture (Ports and Adapters) pattern, separating business logic from framework and infrastructure concerns:

- **Domain Layer**: Contains domain entities, value objects, and business rules (generation context, framework metadata, packaging specifications).
- **Application Layer**: Houses inbound and outbound ports as well as use cases (project generation interactor, validation rules).
- **Infrastructure Layer**: Implements technical adapters for template engines (Apache FreeMarker), archive utilities (ZIP), concrete stack generation strategies (Spring Boot, Express.js, Django, Symfony), database persistence, and remote Git operations (GitHub API).
- **Presentation Layer**: Exposes REST API endpoints and data transfer objects (DTOs), integrated with OpenAPI 3 documentation.

---

## Supported Stacks and Features

- **Backend Frameworks**:
  - Spring Boot (Java 17+, Maven, JPA, Security, Lombok)
  - Express.js (Node.js, CommonJS/ES modules, npm)
  - Django (Python 3, settings, wsgi, requirements)
  - Symfony (PHP 8+, Composer, Kernel, Bundles, PSR-4 namespaces)
- **Database Connectors**: PostgreSQL, MySQL, H2, MongoDB
- **DevOps Scaffolding**: Dockerfile, docker-compose.yml, Jenkinsfile, Kubernetes manifests
- **Git Synchronization**: Automated repository creation and file commits via GitHub REST API
- **Interactive Web Interface**: Angular client with live virtual file tree preview and CLI command generation
- **API Documentation**: Automated Swagger UI and OpenAPI 3 schema generation

---

## System Prerequisites

Before running the application locally, ensure the following software is installed:

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: Version 3.8 or higher (a Maven Wrapper is included in the backend repository)
- **Node.js**: Version 18.x or 20.x LTS
- **npm**: Version 9.x or higher
- **Git**: Version 2.30 or higher
- **Docker & Docker Compose** (Optional): For running generated container environments

---

## Quickstart Guide

### 1. Clone the Repository

```bash
git clone https://github.com/abderrahmanBouanani/Webpick-Initializr.git
cd Webpick-Initializr
```

### 2. Start the Backend Service

Navigate to the backend module directory and launch the Spring Boot application using the Maven Wrapper:

#### On Linux / macOS:
```bash
cd webpick-initializr-code/webpick-initializr-backend
./mvnw clean spring-boot:run
```

#### On Windows (PowerShell):
```powershell
cd webpick-initializr-code\webpick-initializr-backend
.\mvnw.cmd clean spring-boot:run
```

The backend server will start on port `8081`:
- Base API URL: `http://localhost:8081/api/v1`
- Swagger UI Documentation: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI Schema: `http://localhost:8081/v3/api-docs`

To execute backend automated tests:

```bash
./mvnw test
```

### 3. Start the Frontend Application

Open a second terminal window, navigate to the frontend directory, install dependencies, and start the development server:

```bash
cd webpick-initializr-code/webpick-initializr-frontend
npm install
npm start
```

The web interface will be available at `http://localhost:4200`.

### 4. Run with Docker Compose (Single Command)

To build and spin up the complete application stack (Backend and Frontend) in isolated containers:

```bash
docker compose up --build
```

- Web Interface: `http://localhost:4200` (or `http://localhost:80`)
- Backend REST API: `http://localhost:8081/api/v1`
- Swagger UI Documentation: `http://localhost:8081/swagger-ui/index.html`

To run with an attached PostgreSQL database:

```bash
docker compose --profile with-postgres up --build
```

---

## Configuration and Environment Variables

The backend application is configured via `src/main/resources/application.properties` and supports environment variable overrides:

| Variable | Property | Default | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `server.port` | `8081` | HTTP port for the Spring Boot application |
| `SPRING_APPLICATION_NAME` | `spring.application.name` | `webpick-initializr` | Application identifier |

### FreeMarker Engine Settings
- Template resources are stored in `src/main/resources/templates/`.
- Templates are rendered using UTF-8 encoding.

### Remote Git Synchronization
- To push generated projects to GitHub, the request must include `includeGit: true` along with a valid GitHub Personal Access Token (`gitToken`) with the `repo` scope.

For full configuration options, refer to [docs/configuration.md](file:///d:/projects/webpick/docs/configuration.md).

---

## API Reference

The primary endpoint for project bootstrapping is:

### Generate Project Archive

```http
POST /api/v1/projects/generate
Content-Type: application/json
Accept: application/zip
```

#### Sample cURL Request

```bash
curl -X POST http://localhost:8081/api/v1/projects/generate \
  -H "Content-Type: application/json" \
  -H "Accept: application/zip" \
  -d '{
    "projectName": "sample-service",
    "basePackage": "com.webpick.sample",
    "backend": "spring",
    "db": "postgres",
    "deps": ["jpa", "security", "lombok"],
    "devops": ["docker", "docker-compose"],
    "includeGit": false
  }' \
  --output sample-service.zip
```

For complete endpoint documentation and response details, refer to [docs/api.md](file:///d:/projects/webpick/docs/api.md) or open the interactive Swagger UI at `http://localhost:8081/swagger-ui/index.html`.

---

## Project Documentation Structure

All architectural and specification documents are organized in the `/docs` directory:

- [docs/README.md](file:///d:/projects/webpick/docs/README.md): Central index of all documentation.
- [docs/api.md](file:///d:/projects/webpick/docs/api.md): Full REST API specification and examples.
- [docs/configuration.md](file:///d:/projects/webpick/docs/configuration.md): System and environment configuration guide.
- [docs/github-metadata.md](file:///d:/projects/webpick/docs/github-metadata.md): Repository topics, metadata, and release procedure.
- [docs/conception/](file:///d:/projects/webpick/docs/conception/README.md): Architecture diagrams, software modeling, and technical designs.
- [docs/cahier-des-charges/](file:///d:/projects/webpick/docs/cahier-des-charges/README.md): Functional requirements, LaTeX source, and compiled PDF.
- [docs/fiche-technique/](file:///d:/projects/webpick/docs/fiche-technique/README.md): Technical requirements specification, LaTeX source, and compiled PDF.
- [docs/etat-davancement/](file:///d:/projects/webpick/docs/etat-davancement): Milestone presentations and progress reports.
- [CHANGELOG.md](file:///d:/projects/webpick/CHANGELOG.md): Version history and release notes.

---

## Community and Contributing

Contributions are welcome. Please refer to our governance documents before submitting code:

- [Contributing Guidelines](file:///d:/projects/webpick/CONTRIBUTING.md): Environment setup, branching model, Conventional Commits, and pull request procedures.
- [Code of Conduct](file:///d:/projects/webpick/CODE_OF_CONDUCT.md): Community pledge, standards, and enforcement guidelines.
- [Security Policy](file:///d:/projects/webpick/SECURITY.md): Vulnerability reporting procedures and response timeline.

---

## License

This project is licensed under the MIT License. See the [LICENSE](file:///d:/projects/webpick/LICENSE) file for details.
