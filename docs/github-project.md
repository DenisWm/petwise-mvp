---
layout: default
title: GitHub Project
nav_order: 9
---

# GitHub Project
{: .no_toc }

Guidance for configuring a GitHub Project board to manage PetWise implementation work.
{: .fs-6 .fw-300 }

## Project guidance

Use the GitHub Project board to organize and track implementation work. The board should reflect the lifecycle of work items and make it easy to see status and priority at a glance. Recommended columns and their intent:

- Backlog — Candidate work that is not yet prioritized
- Ready — Items that are prepared and prioritized for picking up
- In Progress — Work currently being implemented
- In Review — Pull requests under review or awaiting approvals
- Done — Completed work and closed items

### Collections and fields

- Phase — Single-select field to indicate the high-level phase (Foundation, Domain & Core, Tutor, Pet, Appointment, Queries, Polish)
- Priority — Single-select priorities (P0, P1, P2, P3) to communicate business urgency
- Epic — Free-text or relation field to group related issues under a larger objective
- Story Points (optional) — Intended for relative sizing, not calendar estimates

### Guidance

- Keep the board focused on flow: prefer small, actionable issues that can progress through the board quickly
- Use filters and saved views to support different stakeholders (e.g., by Phase or Assignee)
- Consider automations for common transitions (issue opened → Backlog, issue assigned → In Progress, PR linked → In Review, issue closed → Done)

## Labels

Use a small, consistent set of labels to classify issues by priority, type, and component. Example label sets:

Priority labels
- P0-critical — Blocking
- P1-high — MVP core
- P2-medium — Important but not urgent
- P3-low — Enhancements and future work

Type labels
- infrastructure — Build, CI/CD, deployment
- domain — Domain logic and business rules
- application — Use case implementations
- rest — REST controllers / API layer
- jpa — Data persistence / repositories

Component labels
- tutor
- pet
- appointment

Special labels
- good-first-issue — Suitable for newcomers
- epic — High-level grouping

## Recommended workflow

A lightweight, consistent flow helps keep work visible and predictable. A suggested flow:

- Pick an item from Ready and assign it to a developer
- Create a focused branch and implement changes with tests and documentation updates
- Open a pull request linking the issue; use PR templates and include acceptance criteria
- After review, merge and ensure CI passes; move the issue to Done

Notes
- Use branch naming and PR titles that reference the issue (e.g., `feature/issue-10-create-tutor-use-case`)
- Adapt branching and PR processes to your team's needs; the guidance here is a recommendation rather than a requirement

## Milestones

Milestones group work by high-level phases. Use them to communicate scope and goals, not strict dates. Typical milestones:

- Phase 0: Foundation — Core setup and infrastructure tasks
- Phase 1: Domain Core — Core domain model and validation rules
- Phase 2: Tutor — Tutor-related features and APIs
- Phase 3: Pet — Pet-related features and APIs
- Phase 4: Appointment — Appointment flows and scheduling
- Phase 5: Queries — Read-models, queries, and performance tuning
- Phase 6: Polish — Bug fixes and minor enhancements

## Issue templates

Issue templates are available in `.github/ISSUE_TEMPLATE/`.

## Automations

Recommended automations:
- Auto-add to project: When an issue is opened, add it to Backlog
- Move to In Progress: When an issue is assigned, move it to In Progress
- Move to In Review: When a pull request is linked, move it to In Review
- Move to Done: When an issue is closed, move it to Done

## Views to create

Examples:

- By Phase (Table): Group issues by Phase and sort by Priority
- Current Work (Board): Filter to In Progress or In Review and group by Assignee
- Insights (Chart): Use cumulative flow or other charts to monitor trends

## Metrics to track

- Velocity: Story points completed per period
- Cycle Time: Time from In Progress to Done
- Work in Progress: Monitor WIP and adjust limits as needed

## Quick start commands

Using GitHub CLI:

```bash
# Create an issue
gh issue create --title "Implement CreateTutorUseCase" --label "P1-high,application,tutor,use-case" --milestone "Phase 2: Tutor"

# List issues by label
gh issue list --label "P1-high"

# Create a PR
gh pr create --title "feat: add CreateTutorUseCase (#10)" --body "Closes #10"
```

## Best practices

- Create small, focused issues with clear acceptance criteria
- Link related artifacts (issues → PRs → ADRs)
- Keep the board updated and add progress notes to cards

## How to contribute via the project board

1. Find work on the implementation board (Ready or Backlog)
2. Comment on the issue to indicate you will work on it and request assignment
3. Create a branch and implement changes with tests and documentation updates
4. Open a PR referencing the issue and request review
5. After merge, update the issue status and move the card to Done

## Further reading

- [Implementation Roadmap](implementation-roadmap)
- [Contributing](contributing)
- [GitHub Projects documentation] (https://docs.github.com/en/issues/planning-and-tracking-with-projects)
