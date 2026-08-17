# Security Policy

## Supported Versions

We release patches and security updates for actively maintained versions of Webpick Initializr:

| Version | Supported          |
| ------- | ------------------ |
| 1.x     | Yes                |
| < 1.0   | No                 |

---

## Reporting a Vulnerability

The Webpick Initializr maintainers take security issues seriously. If you discover a security vulnerability, please do NOT create a public issue on GitHub. Instead, follow the responsible disclosure process below:

1. **Email Disclosure**: Send a detailed report to the security contact at:
   `abderrahman.bouanani@webpick.ma`
2. **Include Details**:
   - Type of vulnerability (e.g., Remote Code Execution, Path Traversal, Sensitive Data Exposure).
   - Component affected (backend API, FreeMarker template renderer, Git integration adapter, frontend client).
   - Step-by-step instructions or proof-of-concept payload to reproduce the issue.
   - Any proposed remediation or mitigation steps.

---

## Response Timeline

- **Initial Response**: Within 48 hours of receipt, a maintainer will acknowledge your email and confirm receipt of the report.
- **Triage & Assessment**: Within 7 business days, the team will confirm or reject the vulnerability and determine the severity.
- **Resolution & Disclosure**: We will prepare and test a patch before publicly disclosing the vulnerability. Once resolved, a security advisory will be published along with appropriate credit to the reporter.

---

## Security Best Practices for Self-Hosting

- **Git Token Protection**: Never commit personal access tokens or credentials to version control. Pass tokens via secure client input or secure environment variables.
- **Restricted Network Access**: If exposing the application publicly, place the backend behind an API gateway or reverse proxy (such as Nginx) with rate-limiting configured.
- **Regular Dependency Audits**: Maintainers regularly run automated vulnerability scanners (`mvn dependency-check`, `npm audit`). Contributors and operators are encouraged to keep dependencies updated.
