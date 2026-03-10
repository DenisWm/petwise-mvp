---
layout: default
title: Architecture Decision Records
parent: Architecture
nav_order: 2
---

# Architecture Decision Records (ADRs)
{: .no_toc }

Key architectural decisions documented for future reference.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## What are ADRs?

Architecture Decision Records capture important architectural decisions:

- **Context:** What issue are we facing?
- **Decision:** What did we decide?
- **Consequences:** What are the trade-offs?

{: .note }
> Each ADR lives in its own file under [`architecture/adrs/`](https://github.com/deniswm/petwise-mvp/tree/master/docs/architecture/adrs).
> To create a new ADR, copy [`0000-template.md`](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/adrs/0000-template.md) and follow the instructions inside.

---

## Decision Records

{% include_relative adrs/0001-postgresql-with-h2-for-tests.md %}

---

{% include_relative adrs/0002-appointment-status-lifecycle.md %}

---

{% include_relative adrs/0003-domain-modeling-with-ddd.md %}

---

{% include_relative adrs/0004-repository-and-gateway-strategy.md %}

---

{% include_relative adrs/0005-use-case-pattern.md %}

---

## Creating a New ADR

1. Copy the template:
   ```bash
   cp docs/architecture/adrs/0000-template.md docs/architecture/adrs/NNNN-short-title.md
   ```
2. Fill in all sections (status, context, decision, consequences)
3. Add an `{% raw %}{% include_relative %}{% endraw %}` entry in this page:
   ```markdown
   {% raw %}{% include_relative adrs/NNNN-short-title.md %}{% endraw %}
   ```
4. Commit and push — the ADR will appear on the docs site automatically
