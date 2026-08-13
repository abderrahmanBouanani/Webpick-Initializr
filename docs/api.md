# Webpick Initializr - API Documentation

## Overview

Webpick Initializr exposes a RESTful API to configure, generate, and export application templates across supported backend frameworks, frontend configurations, database connectors, and DevOps setups.

The backend application runs on Spring Boot and exposes OpenAPI 3 / Swagger documentation out of the box.

---

## Interactive API Documentation (Swagger / OpenAPI)

When the backend application is running, the interactive documentation is accessible at:

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON Specification: `http://localhost:8081/v3/api-docs`
- OpenAPI YAML Specification: `http://localhost:8081/v3/api-docs.yaml`

---

## Base URL

```text
http://localhost:8081/api/v1
```

---

## Endpoints

### 1. Generate Project Starter

Generates a customized application archive based on the supplied payload. If remote Git integration is requested, the project is also pushed directly to GitHub.

- **URL**: `/projects/generate`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Accept**: `application/zip`

#### Request Body (`ProjectRequestDTO`)

| Field | Type | Required | Description | Example |
| :--- | :--- | :--- | :--- | :--- |
| `projectName` | `string` | Yes | Name of the project to generate | `"sample-service"` |
| `basePackage` | `string` | Yes | Base package or root namespace | `"com.webpick.sample"` |
| `backend` | `string` | Yes | Target backend framework (`spring`, `express`, `django`, `symfony`) | `"spring"` |
| `frontend` | `string` | No | Frontend template (`angular`, `react`, `vue`, `none`) | `"angular"` |
| `db` | `string` | No | Target database (`postgres`, `mysql`, `h2`, `mongodb`) | `"postgres"` |
| `deps` | `array<string>` | No | List of framework-specific libraries/modules | `["jpa", "security", "lombok"]` |
| `devops` | `array<string>` | No | DevOps scaffolding to include (`docker`, `docker-compose`, `jenkins`, `kubernetes`) | `["docker", "docker-compose"]` |
| `includeGit` | `boolean` | No | Flag to push generated files to remote GitHub repository | `false` |
| `gitToken` | `string` | Conditional | GitHub Personal Access Token (mandatory if `includeGit` is `true`) | `"ghp_yourPersonalAccessToken"` |
| `metadata` | `object` | No | Extra template parameters (e.g. `javaVersion`, `phpVersion`) | `{"javaVersion": "17"}` |

#### Supported Values

##### Backend Frameworks (`backend`)
- `spring`: Java Spring Boot project (Maven, Java 17+, Spring Data JPA, WebMVC).
- `express`: Node.js Express project (JavaScript, npm scripts, index.js, package.json).
- `django`: Python Django project (manage.py, settings, wsgi, requirements.txt).
- `symfony`: PHP Symfony project (composer.json, Kernel.php, bundles.php, HomeController.php).

##### Database Engines (`db`)
- `postgres`: PostgreSQL database configuration.
- `mysql`: MySQL database configuration.
- `h2`: In-memory H2 database (Spring Boot).
- `mongodb`: MongoDB driver / configuration.

##### DevOps Targets (`devops`)
- `docker`: Framework-specific `Dockerfile`.
- `docker-compose`: Multi-container `docker-compose.yml` configured with chosen database.
- `jenkins`: Continuous integration `Jenkinsfile`.
- `kubernetes`: Basic Kubernetes deployment and service manifests.

---

#### Example Request Payload

```json
{
  "projectName": "order-management",
  "basePackage": "com.company.orders",
  "backend": "spring",
  "frontend": "none",
  "db": "postgres",
  "deps": [
    "jpa",
    "security",
    "lombok"
  ],
  "devops": [
    "docker",
    "docker-compose"
  ],
  "includeGit": false,
  "gitToken": "",
  "metadata": {
    "javaVersion": "17"
  }
}
```

#### Example cURL

```bash
curl -X POST http://localhost:8081/api/v1/projects/generate \
  -H "Content-Type: application/json" \
  -H "Accept: application/zip" \
  -d '{
    "projectName": "order-management",
    "basePackage": "com.company.orders",
    "backend": "spring",
    "db": "postgres",
    "deps": ["jpa", "security", "lombok"],
    "devops": ["docker", "docker-compose"],
    "includeGit": false
  }' \
  --output order-management.zip
```

---

#### Response

##### Success: `200 OK`
- **Content-Type**: `application/zip`
- **Content-Disposition**: `attachment; filename="<projectName>.zip"`
- **Body**: Binary ZIP stream containing the assembled directory structure and generated files.

##### Bad Request: `400 BAD REQUEST`
- **Content-Type**: `text/plain`
- **Condition**: Missing or invalid parameters, unsupported framework combination.
- **Body**: Error message describing the validation failure.

##### Internal Server Error: `500 INTERNAL SERVER ERROR`
- **Content-Type**: `text/plain`
- **Condition**: FreeMarker template processing failure or Git remote synchronization failure.
- **Body**: Detailed error trace.

---

## Remote Git Integration Flow

When `includeGit` is set to `true`:
1. The backend validates the presence of `gitToken`.
2. The `GithubApiAdapter` authenticates against `https://api.github.com/user`.
3. A repository named after `projectName` is created under the authenticated user's account.
4. The generated ZIP archive is decompressed in memory.
5. Files are committed to the GitHub repository using the GitHub Contents API with base64 encoding.
6. The client still receives the ZIP download response upon completion.
