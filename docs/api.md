# API reference (MVP)

Base URL `http://localhost:8080/api`
- `GET /dashboard` — outstanding/overdue metrics
- `GET /customers`, `POST /customers`
- `GET /invoices`, `POST /invoices`
- `POST /invoices/{id}/draft-reminder` — returns draft only
- `/actuator/health` — service health

Production API must derive tenant identity from verified auth claims and validate DTOs/permissions.