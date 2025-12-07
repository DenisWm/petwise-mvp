---
layout: default
title: Implementation Roadmap
nav_order: 8
---

# Implementation Roadmap
{: .no_toc }

34 actionable GitHub issues broken down into 6 implementation phases.
{: .fs-6 .fw-300 }

{: .important }
> This roadmap is designed for **distributed development** with GitHub Projects and Issues.

## Table of contents
{: .no_toc .text-delta }

1. TOC
   {:toc}

---

## Project Phases

### Phase 0: Foundation (1-2 weeks)
**Focus:** Project structure, build system, CI/CD

- **Issue #1:** Project Setup and Build Configuration
- **Issue #2:** CI/CD Pipeline with GitHub Actions
- **Issue #3:** Docker & Docker Compose Setup

### Phase 1: Domain & Core (2 weeks)
**Focus:** Domain model, aggregates, entities, validation

- **Issue #4:** Domain DDD Base Classes
- **Issue #5:** Domain Validation Framework
- **Issue #6:** Domain Utilities
- **Issue #7:** Application Use Case Base Classes

### Phase 2: Tutor Management (1-2 weeks)
**Focus:** Complete CRUD for Tutor aggregate

- **Issue #8:** Tutor Domain Model
- **Issue #9:** Tutor Gateway Interface
- **Issue #10:** Create Tutor Use Case
- **Issue #11:** Tutor JPA Entity and Repository
- **Issue #12:** Create Tutor REST Endpoint
- **Issue #13:** List and Get Tutor Endpoints
- **Issue #14:** Update and Delete Tutor Endpoints

### Phase 3: Pet Management (1-2 weeks)
**Focus:** Pet entity within Tutor aggregate

- **Issue #15:** Pet Domain Model
- **Issue #16:** Pet Gateway Interface
- **Issue #17:** Create Pet Use Case and Endpoint
- **Issue #18:** List and Get Pet Endpoints
- **Issue #19:** Update and Delete Pet Endpoints

### Phase 4: Appointment Management (2-3 weeks)
**Focus:** Complex aggregate with lifecycle

- **Issue #20:** Appointment Domain Model
- **Issue #21:** Appointment Gateway Interface
- **Issue #22:** Create Appointment Use Case and Endpoint
- **Issue #23:** Change Appointment Status Use Case and Endpoint
- **Issue #24:** Get and List Appointment Endpoints

### Phase 5: Queries & Reports (1 week)
**Focus:** Daily agenda and search

- **Issue #25:** View Daily Agenda Use Case and Endpoint
- **Issue #26:** Search and Filter Enhancements

### Phase 6: Polish & Documentation (2-3 weeks)
**Focus:** Production readiness

- **Issue #27:** Global Exception Handler
- **Issue #28:** API Documentation with Swagger UI
- **Issue #29:** Database Migration with Flyway
- **Issue #30:** Health Check and Actuator Endpoints
- **Issue #31:** Complete OpenAPI Specification
- **Issue #32:** End-to-End Integration Tests
- **Issue #33:** Performance Testing and Optimization
- **Issue #34:** Final Documentation Review

---

## Implementation Strategy

### Vertical Slices

Each issue implements a complete feature through all layers:

```
REST Controller → Use Case → Domain Logic → Gateway → JPA Repository
```

**Benefits:**
- ✅ Delivers working functionality
- ✅ Can be tested end-to-end
- ✅ Independent development
- ✅ Early integration

### Definition of Done

Each issue must meet:

- [ ] Domain logic implemented with tests
- [ ] Use case implemented with tests
- [ ] REST endpoint implemented
- [ ] Integration test passes
- [ ] OpenAPI spec updated
- [ ] Sequence diagram updated (if applicable)
- [ ] Code formatted (Spotless)
- [ ] Coverage ≥ 80%

---

## Timeline & Estimation

### With 2-3 Developers: 9-13 weeks

| Phase | Duration | Issues | Story Points |
|:------|:---------|:-------|:-------------|
| **Phase 0: Foundation** | 1-2 weeks | 3 | ~8 |
| **Phase 1: Domain Core** | 2 weeks | 4 | ~12 |
| **Phase 2: Tutor** | 1-2 weeks | 7 | ~20 |
| **Phase 3: Pet** | 1-2 weeks | 5 | ~15 |
| **Phase 4: Appointment** | 2-3 weeks | 5 | ~18 |
| **Phase 5: Queries** | 1 week | 2 | ~6 |
| **Phase 6: Polish** | 2-3 weeks | 8 | ~21 |
| **TOTAL** | **9-13 weeks** | **34** | **~100** |

### Velocity Assumptions
- Average 5-8 story points per developer per week
- Some issues can be parallelized
- Includes time for code review and testing

---

## Priority Labels

P0-Critical
{: .label .label-red }
Blocking other work

P1-High
{: .label .label-yellow }
MVP Core

P2-Medium
{: .label .label-blue }
Nice to have

P3-Low
{: .label .label-green }
Future enhancement

### Suggested Priorities

**P0 (Critical):**
- #1, #4, #5, #7 (Foundation for everything else)

**P1 (High - MVP Core):**
- #8-#14 (Tutor management)
- #15-#19 (Pet management)
- #20-#24 (Appointment management)
- #25 (Daily agenda)

**P2 (Medium - Polish):**
- #27, #28, #29, #30 (Infrastructure improvements)
- #31, #32 (Testing & docs)

**P3 (Low - Enhancements):**
- #26 (Search)
- #33 (Performance)

---

## Example Issue: #10 - Create Tutor Use Case

**Labels:** `P1-high`, `application`, `tutor`, `use-case`  
**Epic:** Tutor Management  
**Story Points:** 3

**Description:**
Implement UC-01: Create Tutor use case.

**Tasks:**
- [ ] Create `CreateTutorInput` DTO
- [ ] Create `CreateTutorOutput` DTO
- [ ] Create `CreateTutorUseCase extends UseCase<IN, OUT>`
- [ ] Implement execute() method
- [ ] Add unit tests with mocked gateway

**Acceptance Criteria:**
- [ ] Use case validates input
- [ ] Calls domain factory method
- [ ] Saves via gateway
- [ ] Returns created tutor ID
- [ ] Unit tests with 100% coverage

**Reference:**
- [UC-01: Create Tutor](use-cases/uc-01)
- [Sequence Diagram](https://github.com/deniswm/petwise/blob/main/docs/architecture/sequences/uc01-create-tutor.puml)

---

## Next Steps

1. **GitHub Project** - See [GitHub Project Setup](github-project)
2. **Create Issues** - Use issue templates
3. **Start Development** - Pick issues from Phase 0

---

## Further Reading

- [GitHub Project Setup](github-project-setup) - Configure labels, milestones, workflows
- [Contributing Guide](contributing) - Development guidelines
- [Use Cases](use-cases/) - Detailed use case documentation

