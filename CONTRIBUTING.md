# Contributing to Webpick Initializr

Thank you for your interest in contributing to Webpick Initializr. We welcome contributions from developers of all skill levels.

To maintain codebase quality, architectural clarity, and smooth collaboration, please follow the guidelines outlined below.

---

## Code of Conduct

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md).

---

## Development Environment Setup

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Node.js 18.x or 20.x LTS with npm 9+
- Git 2.30 or higher
- Optional: Docker and Docker Compose

### 1. Fork and Clone

```bash
git clone https://github.com/<your-username>/Webpick-Initializr.git
cd Webpick-Initializr
```

### 2. Backend Setup

The backend is built with Spring Boot and uses Maven:

```bash
cd webpick-initializr-code/webpick-initializr-backend

# On Linux / macOS:
./mvnw clean test

# On Windows:
.\mvnw.cmd clean test
```

To run the backend development server on port 8081:

```bash
./mvnw spring-boot:run
```

### 3. Frontend Setup

The frontend is an Angular application:

```bash
cd webpick-initializr-code/webpick-initializr-frontend
npm install
npm start
```

The web application runs at `http://localhost:4200`.

---

## Architecture Principles

Webpick Initializr strictly enforces **Hexagonal Architecture (Ports and Adapters)**:

- **Domain Layer (`domain`)**:
  - Independent of Spring, databases, or external libraries.
  - Contains core entities (`GenerationContext`), value objects, and business rules.
- **Application Layer (`application`)**:
  - Contains inbound use cases (`IGenerateProjectUseCase`), outbound ports (`ITemplateEnginePort`, `IArchivePort`, `IGitRemotePort`), and service interactors.
- **Infrastructure Layer (`infrastructure`)**:
  - Contains technology adapters (FreeMarker, Zip archiving, GitHub API, database repositories).
- **Presentation Layer (`presentation`)**:
  - Exposes REST controllers, DTOs, mappers, and OpenAPI documentation.

When adding features:
1. Define ports (interfaces) before writing technical implementations.
2. Keep domain logic pure and framework-agnostic.
3. Add automated tests for every new adapter or use case.

---

## Git Workflow and Branching Model

We follow a feature-branch workflow:

1. Always create a new branch from `main`:
   ```bash
   git checkout main
   git pull origin main
   git checkout -b <type>/<short-description>
   ```

### Branch Naming Conventions

Use one of the following prefixes:
- `feat/` for new features (e.g., `feat/add-fastapi-generator`)
- `fix/` for bug fixes (e.g., `fix/dockerfile-entrypoint-permission`)
- `docs/` for documentation updates (e.g., `docs/update-api-guide`)
- `refactor/` for code refactoring without behavior change (e.g., `refactor/extract-zip-service`)
- `test/` for adding or updating tests (e.g., `test/symfony-controller-tests`)
- `chore/` for tooling or dependency updates (e.g., `chore/upgrade-springdoc`)

---

## Commit Message Conventions

We adhere to the **Conventional Commits** specification:

```text
<type>(<scope>): <short description in present tense>

[optional body explaining motivation and context]

[optional footer(s), e.g., Closes #123]
```

### Allowed Types
- `feat`: A new feature for the user or generator
- `fix`: A bug fix
- `docs`: Documentation changes only
- `style`: Changes that do not affect the meaning of the code (formatting, white-space)
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `perf`: Code change that improves performance
- `test`: Adding missing tests or correcting existing tests
- `chore`: Changes to build process, dependencies, or auxiliary tools

### Examples
- `feat(generator): add support for Go Gin starter templates`
- `fix(git): handle special characters in repository names`
- `docs(api): document query parameters for project archive endpoint`
- `test(symfony): verify composer bundles configuration`

---

## Code Quality Standards

Before opening a pull request, run the following verification checks:

### Backend Checks
```bash
cd webpick-initializr-code/webpick-initializr-backend
./mvnw clean verify
./mvnw checkstyle:check
```

### Frontend Checks
```bash
cd webpick-initializr-code/webpick-initializr-frontend
npm run build
npm test -- --watch=false --browsers=ChromeHeadless
```

---

## Pull Request Guidelines

1. **Title**: Follow Conventional Commits format (e.g., `feat(devops): add Kubernetes manifests generator`).
2. **Description**: Fill out the provided Pull Request template completely.
3. **Automated Tests**: Ensure all automated unit and integration tests pass in CI.
4. **Scope**: Keep pull requests focused on a single issue or feature. Avoid bundling unrelated refactorings.
5. **Review**: Address reviewer feedback promptly. Once approved, pull requests will be squashed and merged.
