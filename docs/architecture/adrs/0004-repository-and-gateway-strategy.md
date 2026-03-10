### ADR-0004: Repository and Gateway Strategy
{: .no_toc }

**Date:** 2025-01-01

**Status:** ✅ Accepted

**Context:**  
Need persistence abstraction without coupling to JPA.

**Decision:**  
Use domain-level gateway interfaces as ports:
- Domain defines contracts (e.g., `TutorGateway`, `PetGateway`, `AppointmentGateway`)
- Infrastructure implements adapters (e.g., `TutorPostgresGateway`)

**Consequences:**
- ✅ Domain independent of persistence technology
- ✅ Easy to test with in-memory implementations
- ✅ Can swap JPA for other solutions

