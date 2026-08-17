## Description

Please include a summary of the change, relevant motivation, and context.
List any dependencies that are required for this change.

Closes #(issue)

## Type of Change

Please delete options that are not relevant:

- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring or code quality enhancement
- [ ] CI/CD or tooling adjustment

## Architecture Alignment

- [ ] Follows Hexagonal Architecture principles (Ports and Adapters).
- [ ] Domain logic remains framework-agnostic.
- [ ] New ports are accompanied by concrete adapters and automated unit tests.

## How Has This Been Tested?

Please describe the tests that you ran to verify your changes. Include details of test configurations:

- [ ] Backend unit and integration tests (`./mvnw clean test`)
- [ ] Code coverage verified with JaCoCo
- [ ] Checkstyle passed (`./mvnw checkstyle:check`)
- [ ] Frontend build and tests passed (`npm run build`, `npm test`)
- [ ] Manual verification via Swagger UI or web interface

## Checklist

- [ ] My code follows the style guidelines of this project.
- [ ] I have performed a self-review of my own code.
- [ ] I have commented my code, particularly in hard-to-understand areas.
- [ ] I have made corresponding changes to the documentation (`/docs` or `README.md`).
- [ ] My changes generate no new warnings.
- [ ] I have added tests that prove my fix is effective or that my feature works.
- [ ] New and existing automated tests pass locally with my changes.
- [ ] Any dependent changes have been merged and published in downstream modules.
