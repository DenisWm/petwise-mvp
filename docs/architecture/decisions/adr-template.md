# ADR-XXXX – [Short Title of Decision]

- **Status**: [Proposed | Accepted | Deprecated | Superseded]
- **Date**: YYYY-MM-DD
- **Deciders**: [Names or roles of people who made this decision]
- **Supersedes**: [ADR-YYYY] (if applicable)
- **Superseded by**: [ADR-ZZZZ] (if applicable)

---

## Context

**What is the issue we're facing?**

Describe the context and the problem that requires a decision. Include:
- Current situation
- Forces at play (technical, organizational, team skills)
- Constraints (time, budget, technology)
- Why this decision is necessary

Keep this section focused on **why** we need to make a decision, not **what** the decision is.

---

## Decision

**What have we decided to do?**

Clearly state the decision in one or two sentences.

Then elaborate:
- What approach or pattern will we use?
- How will it be implemented?
- What are the key aspects of this decision?

Be specific and concrete. Include code examples or diagrams if helpful.

---

## Consequences

**What are the implications of this decision?**

### Positive

List the benefits and advantages:
- What problems does this solve?
- What improvements does it bring?
- What new capabilities does it enable?

### Negative

List the drawbacks and trade-offs:
- What complexity does it add?
- What flexibility do we lose?
- What new problems might it create?

### Neutral

List side effects that are neither clearly positive nor negative:
- What changes as a result?
- What stays the same?

---

## Alternatives Considered

**What other options did we evaluate?**

For each alternative:

### Alternative 1: [Name]

**Description:** What is this approach?

**Pros:**
- Advantage 1
- Advantage 2

**Cons:**
- Disadvantage 1
- Disadvantage 2

**Reason for rejection:** Why didn't we choose this?

### Alternative 2: [Name]

(Repeat structure above)

---

## References

**What influenced this decision?**

- Links to related documentation
- External articles or papers
- Related ADRs
- Issue tracker tickets
- Design documents

---

## Notes

**Additional context or future considerations**

- Implementation notes
- Migration strategy (if replacing something)
- Review date (if temporary)
- Open questions or future work

---

## Example ADR (Delete This Section)

Here's a concrete example to guide you:

---

# ADR-0007 – Use JWT for API Authentication

- **Status**: Accepted
- **Date**: 2025-11-27
- **Deciders**: Backend Team

## Context

PetWise currently has no authentication mechanism. All API endpoints are public.

For the next iteration, we need to:
- Authenticate users (attendants and managers)
- Authorize access to sensitive operations
- Support both web UI and mobile clients
- Keep the solution simple for an MVP

Team skills:
- Familiar with Spring Security
- Limited DevOps resources (no OAuth2 server setup)

## Decision

We will use **JWT (JSON Web Tokens)** for stateless authentication.

Implementation:
- Spring Security for authentication/authorization
- JWT tokens issued on login (POST /api/v1/auth/login)
- Tokens validated on each request via Bearer header
- User roles stored in JWT claims (ATTENDANT, MANAGER)

Example:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Consequences

### Positive
- **Stateless** – No session storage required
- **Scalable** – Works across multiple instances
- **Simple** – No external auth server needed for MVP
- **Standard** – Well-supported by clients and libraries

### Negative
- **Token Revocation** – Cannot easily invalidate tokens before expiry
- **Token Size** – Larger than session IDs (sent with every request)
- **Secret Management** – Must securely store signing key

### Neutral
- Token expiry requires refresh mechanism in future

## Alternatives Considered

### Alternative 1: Session-based Authentication

**Pros:**
- Easy to invalidate (destroy session)
- Smaller cookies

**Cons:**
- Requires session storage (Redis, sticky sessions)
- Harder to scale horizontally
- Not suitable for mobile apps

**Reason for rejection:** Doesn't align with stateless architecture goals

### Alternative 2: OAuth2 with External Provider

**Pros:**
- Industry standard
- Delegated authentication (Google, GitHub, etc.)

**Cons:**
- Overkill for MVP
- Requires external dependency
- More complex setup

**Reason for rejection:** Too complex for current needs

## References

- [RFC 7519 - JWT](https://tools.ietf.org/html/rfc7519)
- [Spring Security JWT Guide](https://spring.io/guides/tutorials/spring-boot-oauth2/)

## Notes

- Token expiry set to 24 hours for MVP
- Refresh token mechanism planned for next iteration
- Review security best practices before production deployment

---

**End of example. Delete this section when creating a real ADR.**

