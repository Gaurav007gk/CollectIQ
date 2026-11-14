# CollectIQ architecture

Browser -> Next.js UI -> Spring Boot API -> PostgreSQL. The API calls a Python agent service to generate drafts. MCP server exposes bounded tools for compatible clients. Redis supports cache/rate limits; Kafka/outbox can be introduced for async jobs.

## Scale roadmap
- Stateless API/agent replicas behind ingress; autoscaling on CPU/latency/queue depth.
- Tenant-scoped indexes, connection pooling, read replicas, and PostgreSQL RLS.
- Transactional outbox + Kafka, idempotent workers for imports/reminders.
- Redis for rate limits and short locks; never as source of truth.
- S3-compatible object storage for CSV/PDF with signed URLs and malware scan.
- OpenTelemetry traces, structured logs, metrics, SLO alerts.
- Kubernetes/Helm + managed database/queue for production.

## Safety
Draft only by default. Human approval required before any outbound WhatsApp/SMS/email. Tenant identity must come from authenticated claims, not request bodies.
