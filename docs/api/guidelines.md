# API Guidelines (PetWise)

- Base path: `/api/v1`
- Style: REST, resource-oriented
- Format: JSON for requests and responses
- Validation:
    - Bean Validation in the backend
    - 400 Bad Request for validation errors
- Errors:
    - Use `application/problem+json` (RFC 7807)
    - Include `type`, `title`, `status`, `detail`, `instance`
- Pagination:
    - `page` (0-based), `size` (1–200)
- Date/time:
    - `date`: ISO 8601 (`YYYY-MM-DD`)
    - `date-time`: ISO 8601 with timezone (`YYYY-MM-DDTHH:mm:ssZ` or offset)