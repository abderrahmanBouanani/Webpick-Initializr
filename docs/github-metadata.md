# GitHub Repository Metadata Guide

This document outlines the recommended configuration for the repository's **About** section on GitHub.

---

## Repository Details

### Description
```text
Open-source project generator and scaffolding platform built with Hexagonal Architecture (Spring Boot, Angular, FreeMarker, DevOps, Git integration).
```

### Website / Demo URL
```text
https://github.com/abderrahmanBouanani/Webpick-Initializr
```

---

## Repository Topics (Tags)

Configure the following topics in the GitHub repository settings to enhance searchability and ecosystem discoverability:

- `spring-boot`
- `initializr`
- `code-generator`
- `hexagonal-architecture`
- `devops`
- `angular`
- `freemarker`
- `project-generator`
- `scaffolding`
- `openapi`
- `swagger`
- `docker`
- `rest-api`
- `java-17`

---

## Configuration via GitHub CLI

To apply these settings directly using the GitHub CLI (`gh`), execute:

```bash
gh repo edit abderrahmanBouanani/Webpick-Initializr \
  --description "Open-source project generator and scaffolding platform built with Hexagonal Architecture (Spring Boot, Angular, FreeMarker, DevOps, Git integration)." \
  --homepage "https://github.com/abderrahmanBouanani/Webpick-Initializr" \
  --add-topic "spring-boot" \
  --add-topic "initializr" \
  --add-topic "code-generator" \
  --add-topic "hexagonal-architecture" \
  --add-topic "devops" \
  --add-topic "angular" \
  --add-topic "freemarker" \
  --add-topic "project-generator" \
  --add-topic "scaffolding" \
  --add-topic "openapi" \
  --add-topic "swagger" \
  --add-topic "docker" \
  --add-topic "rest-api" \
  --add-topic "java-17"
```

---

## Release and Tagging Strategy

Webpick Initializr follows [Semantic Versioning (SemVer)](https://semver.org):

- **Major (`vX.0.0`)**: Breaking architectural changes or incompatible API modifications.
- **Minor (`v1.X.0`)**: Backwards-compatible new features (e.g. new stack generators, new DevOps adapters).
- **Patch (`v1.0.X`)**: Backwards-compatible bug fixes and documentation updates.

### Creating a Release Tag

```bash
git tag -a v1.0.0 -m "Release v1.0.0: Initial public open-source release"
git push origin v1.0.0
```

Pushing the tag triggers the automated `.github/workflows/release.yml` pipeline which builds the project assets and publishes the GitHub Release with attached binaries.
