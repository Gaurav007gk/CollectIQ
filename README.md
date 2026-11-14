# CollectIQ — AI Collections SaaS for Indian MSMEs

A polished receivables workspace for tracking invoices, customers, overdue balances and AI-assisted payment reminders.

## Product preview
- Sidebar SaaS shell, responsive layout, KPI cards, collection health, invoice tables and customer cards.
- Customer create flow and invoice create flow.
- Invoice search/status filtering and CSV bulk import.
- AI reminder drafting per invoice; generated copy remains review-only.
- Activity timeline and workspace settings.
- INR formatting and seeded demo workspace.

## Stack
- **Frontend:** Next.js 14, React 18, TypeScript, responsive custom UI
- **API:** Java 21, Spring Boot 3, Spring Data JPA, Flyway
- **AI:** FastAPI agent; optional OpenAI Chat Completions API (fallback draft works without key)
- **MCP:** Python MCP server with summary and reminder-draft tools
- **Database:** PostgreSQL 16
- **Local infra:** Docker Compose + Redis
- **CI:** GitHub Actions (web build + API Maven package)

## Start
```bash
git clone https://github.com/Gaurav007gk/CollectIQ.git
cd CollectIQ
# Optional: configure OPENAI_API_KEY and LLM_MODEL in your shell/.env
docker compose up --build
```
Open http://localhost:3000. API health: http://localhost:8080/actuator/health. Agent health: http://localhost:8000/health.

The API seeds a demo workspace on first boot. CSV import expects headers: `invoiceNumber,customerId,amount,dueDate`; customer IDs are available from the Customers API (`GET /api/customers`). Amounts are INR.

## API
- `GET/POST /api/customers`
- `GET/POST /api/invoices`
- `POST /api/invoices/bulk`
- `PUT /api/invoices/{id}/status` (OPEN or PAID)
- `GET /api/dashboard`
- `GET /api/activity`
- `POST /api/invoices/{id}/draft-reminder` (draft only; never sends)

## MCP
Install `services/mcp/requirements.txt` and run `python services/mcp/server.py` (from services/mcp). Tools are demo-only; connect them to authenticated API calls before production.

## Production readiness — honest status
This repository is a feature-rich **demo/MVP**, not yet a secure production SaaS. Before real customer use, implement OIDC/JWT and RBAC, tenant isolation/RLS, audit persistence, approval inbox, actual WhatsApp/email delivery, consent/opt-out, robust CSV parsing/validation, idempotent background workers, test coverage, monitoring and deployment secrets. AI output must always be reviewed before sending. See [security](docs/security.md) and [architecture](docs/architecture.md).


## Production-oriented foundations (new)
- Google Identity Services sign-in page (`/login`) obtains a Google ID token; dashboard requests send the token as Bearer.
- Spring Security validates JWT signature, issuer and expiry against Google JWKS; API endpoints require authentication.
- Request tenant context is derived from the authenticated token subject (not a client-supplied tenant header) for customer/invoice CRUD.
- Scheduled overdue scan queues reminders for review; it never auto-sends.
- Approval queue endpoints: `GET/POST /api/approvals`, `POST /api/approvals/{id}/approve|reject`.
- WhatsApp Cloud API adapter sends only an approved, unsent approval ID, using credentials from environment; it records sent status/audit.
- Optional deployment templates: Render blueprint (`render.yaml`) and Kubernetes namespace/API manifests (`deploy/k8s`).
- Security workflow: CodeQL + dependency review.

### Configure Google sign-in
1. Create OAuth 2.0 Web client in Google Cloud Console.
2. Add your local and deployed origins to Authorized JavaScript origins.
3. Set `NEXT_PUBLIC_GOOGLE_CLIENT_ID` in web build/runtime environment (Compose supports it).
4. Open `/login`; sign in with a Google account. Token is stored in session storage for the browser session.

### WhatsApp Cloud API
Set `WHATSAPP_TOKEN`, `WHATSAPP_PHONE_NUMBER_ID`, and `WHATSAPP_API_VERSION` as deployment secrets. Complete Meta Business verification, phone registration, messaging template approval and consent/opt-out policy before contacting real customers. Reminder sends are only through `/api/whatsapp/send-approved` with `{ "approvalId": 123 }` after an approval is recorded.

### Deployment (templates, not a live deployment)
- Render: connect the GitHub repo, create services from Blueprint, configure managed PostgreSQL and secrets. Configure frontend `NEXT_PUBLIC_API_URL` with the deployed API URL and allowed CORS origin.
- Kubernetes: build/push API image to GHCR, create namespace, create `collectiq-api-secrets` from your secret manager, then apply `deploy/k8s/api.yaml`. Add managed Postgres, ingress/TLS, web/agent deployments, autoscaling, network policies and external secrets before production.
- Never place provider credentials in Git or frontend variables. Use a secret manager and rotate credentials.

**Production caveat:** These are security and integration foundations, not a certification that the service is production-ready. A production rollout still needs a full tenant-bound authorization review (including workflow/approval ownership), schema migrations and integration tests, rate limits, idempotent delivery/retry + webhook reconciliation, consent/opt-out enforcement, secure admin/roles, backups/restore drills, observability, load testing and penetration testing. Run CI and validate on a staging environment before using customer data.
