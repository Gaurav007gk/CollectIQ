# CollectIQ — AI Collection Employee for MSMEs

CollectIQ helps Indian distributors and small businesses manage receivables, overdue invoices, payment promises and follow-ups.

## MVP stack
- **Web:** Next.js 14 + TypeScript
- **API:** Java 21 + Spring Boot 3 + Spring Data JPA + Flyway
- **Agent:** Python 3.12 + FastAPI (safe reminder drafting)
- **MCP:** Python MCP server with collection summary and draft tools
- **Data/infra:** PostgreSQL, Redis, Docker Compose; Kafka is a scale-out extension

> MVP is demo-oriented. AI only drafts messages; outbound communication requires human approval. Authentication/tenant hardening and real WhatsApp integration are not yet production-ready.

## Run locally
```bash
docker compose up --build
```
- UI: http://localhost:3000
- API: http://localhost:8080/api
- Health: http://localhost:8080/actuator/health
- Agent: http://localhost:8000/health

Sample invoices are seeded automatically on first API startup.

## Features in this starter
- Receivables dashboard (outstanding, overdue, invoice count)
- Invoice list and sample customer data
- Reminder draft generation through agent service
- REST endpoints for customers and invoices
- PostgreSQL schema and indexes via Flyway
- MCP tools (read summary + draft; no send tool)
- Dockerfiles and CI workflow

## Layout
`apps/web` dashboard · `services/api` Spring API · `services/agent` AI workflow · `services/mcp` MCP tools · `docs` architecture/security

## Production roadmap
OIDC/JWT and RBAC, strict tenant isolation/RLS, human approval inbox, WhatsApp Business API adapter, outbox + Kafka workers, Redis rate limiting, S3 imports, audit trails, OpenTelemetry, Kubernetes/Helm, automated tests and security scanning.

See [architecture](docs/architecture.md) and [security](docs/security.md).