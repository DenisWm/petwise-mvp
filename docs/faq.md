---
layout: default
title: FAQ
nav_order: 99
description: "Frequently asked questions"
---

# FAQ

**Q: Where do I report issues?**

Use the [repository issues page](https://github.com/deniswm/petwise-mvp/issues).

**Q: How do I render diagrams?**

```bash
make diagrams
```

This renders `.puml` files and copies generated PNGs to `docs/assets/diagrams/`.

**Q: Where are the developer setup instructions?**

See the [Getting Started](getting-started) guide.

**Q: What database does PetWise use?**

PostgreSQL for runtime (via Docker Compose) and H2 for integration tests. See [ADR-0001](architecture/decisions#adr-0001-postgresql-with-h2-for-tests).

**Q: Why are there no service classes?**

PetWise uses the **Use Case pattern** instead. Each application-layer operation is a single-purpose class (e.g., `DefaultCreateTutorUseCase`). See [ADR-0005](architecture/decisions#adr-0005-use-case-pattern).

**Q: Where is the OpenAPI spec?**

At `docs/api/openapi.yaml`. When running the application, Swagger UI is available at `http://localhost:8080/swagger-ui.html`.
