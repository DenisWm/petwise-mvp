### ADR-0001: PostgreSQL with H2 for Tests

- ⚠️ Requires Docker for local development
- ✅ Flyway ensures consistent schema across environments
- ✅ H2 provides fast, zero-config test execution
- ✅ Production-grade database from the start
**Consequences:**

Use PostgreSQL as the primary database (via Docker Compose) and H2 as an in-memory database for integration tests. Flyway manages schema migrations.
**Decision:**  

Need a reliable database for the MVP with easy test setup.
**Context:**  

**Status:** ✅ Accepted

**Date:** 2025-01-01

{: .no_toc }
