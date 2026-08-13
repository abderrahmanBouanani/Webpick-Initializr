# Webpick Initializr - Configuration Guide

## Overview

Webpick Initializr is configurable through Spring Boot application properties (`application.properties` or `application.yml`), environment variables, and frontend environment files.

This guide details all configuration options for both local development and production environments.

---

## Backend Configuration

### Server Properties

The default application configuration file is located at:
`webpick-initializr-code/webpick-initializr-backend/src/main/resources/application.properties`

| Property | Default Value | Environment Variable | Description |
| :--- | :--- | :--- | :--- |
| `server.port` | `8081` | `SERVER_PORT` | HTTP port used by the Spring Boot server |
| `spring.application.name` | `webpick-initializr` | `SPRING_APPLICATION_NAME` | Logical application name |

Example override via environment variables:

```bash
export SERVER_PORT=8082
export SPRING_APPLICATION_NAME=custom-initializr
```

---

### FreeMarker Template Engine Configuration

Webpick Initializr uses Apache FreeMarker to dynamically render project skeletons.

- **Adapter**: `com.webpick.initializr.generation.infrastructure.adapters.FreeMarkerEngineAdapter`
- **Template Directory**: Classpath `/templates` (`src/main/resources/templates`)
- **Encoding**: `UTF-8`
- **Template Exception Handler**: `TemplateExceptionHandler.RETHROW_HANDLER`
- **Fallback on Null Variable**: `false`

#### Template Directory Structure

```text
src/main/resources/templates/
├── devops/
│   ├── Dockerfile.ftl
│   ├── Jenkinsfile.ftl
│   └── docker-compose.yml.ftl
├── django/
│   ├── manage.py.ftl
│   ├── settings.py.ftl
│   └── urls.py.ftl
├── express/
│   ├── index.js.ftl
│   └── package.json.ftl
├── spring/
│   ├── Application.java.ftl
│   ├── application.properties.ftl
│   └── pom.xml.ftl
└── symfony/
    ├── HomeController.php.ftl
    ├── Kernel.php.ftl
    ├── bundles.php.ftl
    ├── composer.json.ftl
    ├── doctrine.yaml.ftl
    ├── index.php.ftl
    └── routes.yaml.ftl
```

---

### Remote Git Module Configuration

The Git module pushes generated projects to a remote GitHub account upon user request.

- **Adapter**: `com.webpick.initializr.git.infrastructure.GithubApiAdapter`
- **Target Remote Service**: GitHub REST API (`https://api.github.com`)
- **API Version**: `2022-11-28`
- **Authentication**: Bearer token via GitHub Personal Access Token (PAT)

#### GitHub Token Permissions
To allow repository creation and file upload, the token provided in the request payload must contain the following scope:
- `repo` (Full control of private repositories)

#### Flow and Error Handling
- The adapter queries `https://api.github.com/user` to resolve the username.
- It calls `POST https://api.github.com/user/repos` to create the target repository (defaulting to private).
- If the repository already exists, the adapter logs a message and proceeds to upload files.
- Each generated file is encoded in Base64 and committed via `PUT https://api.github.com/repos/{owner}/{repo}/contents/{path}`.

---

### Database and Persistence Configuration

Webpick Initializr includes Spring Data JPA and the H2 in-memory database runtime dependency for storing metadata or configurations.

#### In-Memory H2 Configuration (Default)

When running locally without external databases, Spring Boot automatically configures an in-memory H2 database:

```properties
spring.datasource.url=jdbc:h2:mem:webpickdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

#### PostgreSQL Configuration (Optional)

To use PostgreSQL in production or staging environments:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/webpick
spring.datasource.username=postgres
spring.datasource.password=secret
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

### CORS (Cross-Origin Resource Sharing)

The generation controller enables CORS for the Angular frontend:

- **Allowed Origins**: `http://localhost:4200`
- **Annotated Controller**: `InitializrController` with `@CrossOrigin(origins = "http://localhost:4200")`
- **Production Setting**: Update the allowed origin in `InitializrController` or define a global `WebMvcConfigurer` bean for multiple origins.

---

## Frontend Configuration

The Angular application is located at:
`webpick-initializr-code/webpick-initializr-frontend/`

- **Port**: `4200` (default for `ng serve`)
- **Backend API URL**: Configured in frontend services pointing to `http://localhost:8081/api/v1`
- **Package Manager**: npm (v9+)
