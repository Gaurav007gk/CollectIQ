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
