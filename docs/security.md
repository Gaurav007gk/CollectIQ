# Production security checklist

- Add OIDC/JWT, RBAC (OWNER/FINANCE/VIEWER), and tenant isolation at every repository boundary.
- Enforce tenant_id from verified claims; consider PostgreSQL RLS.
- Secrets via Secret Manager/Vault; rotate and never commit credentials.
- Encrypt transport/storage; minimize PII and define retention/deletion.
- Audit imports, AI drafts, approvals, sends and payment status changes.
- Respect consent, opt-outs, quiet hours, channel rate limits and applicable laws.
- Validate CSV size/schema; scan uploads; dependency/container/secret scanning in CI.
- Treat AI output as untrusted and require approval for external side effects.
