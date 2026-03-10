### ADR-0002: Appointment Status Lifecycle Model
{: .no_toc }

**Date:** 2025-01-01

**Status:** ✅ Accepted

**Context:**  
Appointments need a clear state machine with valid transitions.

**Decision:**  
Implement a forward-only status lifecycle with transition validation in the aggregate:

```
PENDING → ACTIVE → COMPLETED
PENDING → CANCELED
```

**Valid transitions:**
- `PENDING` → `ACTIVE`
- `PENDING` → `CANCELED`
- `ACTIVE` → `COMPLETED`

No backward transitions. `COMPLETED` and `CANCELED` are terminal states.

**Consequences:**
- ✅ Business rules enforced at domain level
- ✅ Clear lifecycle prevents invalid states
- ✅ Simple and predictable

