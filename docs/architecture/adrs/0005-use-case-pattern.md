### ADR-0005: Use Case Pattern
{: .no_toc }

**Date:** 2025-01-01

**Status:** ✅ Accepted

**Context:**  
Need consistent structure for application layer operations.

**Decision:**  
Every use case follows single responsibility:
```java
public abstract class UseCase<INPUT, OUTPUT> {
    public abstract OUTPUT execute(INPUT input);
}
```

Variants: `UnitUseCase<INPUT>` (no output), `NullaryUseCase<OUTPUT>` (no input).

**Consequences:**
- ✅ Clear, testable, single-purpose operations
- ✅ 1:1 mapping between documented use cases and code
- ℹ️ One class per use case (increases file count)

