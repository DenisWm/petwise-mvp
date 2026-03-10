### ADR-0003: Domain Modeling with DDD Basics
{: .no_toc }

**Date:** 2025-01-01

**Status:** ✅ Accepted

**Context:**  
Need clear separation between entities, value objects, and aggregates.

**Decision:**  
Apply DDD tactical patterns:
- **Aggregate Roots:** `Tutor`, `Appointment` — consistency boundaries
- **Entities:** `Pet` — has identity, lives inside Tutor aggregate
- **Value Objects:** `Email`, `Phone`, `ServiceType`, `AppointmentStatus` — immutable

**Consequences:**
- ✅ Clear business logic encapsulation
- ✅ Testable domain model
- ℹ️ Requires discipline to maintain boundaries

