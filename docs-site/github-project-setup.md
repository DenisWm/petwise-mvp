---
layout: default
title: GitHub Project Setup
nav_order: 9
---

# GitHub Project Setup
{: .no_toc }

Configure GitHub Issues and Projects for managing PetWise implementation.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## GitHub Projects Setup

### Step 1: Create a GitHub Project

1. Go to your repository → **Projects** tab
2. Click **New Project**
3. Select **Board** view
4. Name: "PetWise MVP Development"

### Step 2: Configure Board Columns

| Column | Purpose |
|:-------|:--------|
| **📋 Backlog** | Issues not yet started |
| **🔜 Ready** | Issues ready to be picked up |
| **🚧 In Progress** | Currently being worked on |
| **👀 In Review** | Waiting for PR review |
| **✅ Done** | Completed issues |

### Step 3: Add Custom Fields

| Field | Type | Options |
|:------|:-----|:--------|
| **Phase** | Single select | Foundation, Domain & Core, Tutor, Pet, Appointment, Queries, Polish |
| **Story Points** | Number | 1-8 |
| **Epic** | Text | Epic name |
| **Priority** | Single select | P0-Critical, P1-High, P2-Medium, P3-Low |

---

## Labels Setup

### Create Labels

```bash
# Priority labels
gh label create "P0-critical" --color "d73a4a" --description "Blocking"
gh label create "P1-high" --color "ff6b6b" --description "MVP Core"
gh label create "P2-medium" --color "ffa500" --description "Nice to have"
gh label create "P3-low" --color "ffd700" --description "Future"

# Type labels
gh label create "infrastructure" --color "0366d6" --description "Build, CI/CD"
gh label create "domain" --color "1d76db" --description "Domain logic"
gh label create "application" --color "5319e7" --description "Use cases"
gh label create "rest" --color "8b5cf6" --description "REST controllers"
gh label create "jpa" --color "ec4899" --description "JPA repositories"

# Component labels
gh label create "tutor" --color "22c55e"
gh label create "pet" --color "10b981"
gh label create "appointment" --color "14b8a6"

# Special labels
gh label create "good-first-issue" --color "7057ff"
gh label create "epic" --color "3b82f6"
```

---

## Workflow

### For Developers

**1. Pick an Issue**
- Go to Projects board
- Pick from "Ready" column
- Assign yourself
- Issue auto-moves to "In Progress"

**2. Create Branch**
```bash
git checkout -b feature/issue-10-create-tutor-use-case
```

**3. Implement**
- Follow Definition of Done
- Write tests
- Update docs

**4. Create Pull Request**
```bash
gh pr create --title "feat: implement CreateTutorUseCase (#10)" \
             --body "Closes #10"
```

- Issue auto-moves to "In Review"

**5. Merge**
- After review and approval
- Issue auto-closes and moves to "Done"

---

## Milestones

Create milestones for each phase:

| Milestone | Due Date | Issues |
|:----------|:---------|:-------|
| **Phase 0: Foundation** | Week 2 | #1-3 |
| **Phase 1: Domain Core** | Week 4 | #4-7 |
| **Phase 2: Tutor** | Week 6 | #8-14 |
| **Phase 3: Pet** | Week 8 | #15-19 |
| **Phase 4: Appointment** | Week 11 | #20-24 |
| **Phase 5: Queries** | Week 12 | #25-26 |
| **Phase 6: Polish** | Week 14 | #27-34 |

---

## Issue Templates

Issue templates are already created in `.github/ISSUE_TEMPLATE/`:

- **feature.yml** - Feature implementation
- **bug.yml** - Bug report
- **documentation.yml** - Documentation update
- **epic.yml** - Epic tracking

---

## Automations

### Recommended Automations

**Auto-add to project:**
- When: Issue opened
- Then: Add to "Backlog" column

**Move to In Progress:**
- When: Issue assigned
- Then: Move to "In Progress"

**Move to In Review:**
- When: Pull request linked
- Then: Move to "In Review"

**Move to Done:**
- When: Issue closed
- Then: Move to "Done"

---

## Views to Create

### View 1: By Phase (Table)

**Purpose:** See all issues grouped by implementation phase

**Configuration:**
- Layout: Table
- Group by: Phase
- Sort by: Priority
- Show: Title, Assignees, Status, Story Points

### View 2: Current Sprint (Board)

**Purpose:** Focus on current work

**Configuration:**
- Layout: Board
- Filter: Status = "In Progress" OR "In Review"
- Group by: Assignee

### View 3: Burndown (Insights)

**Purpose:** Track velocity

**Configuration:**
- Chart: Cumulative flow
- X-axis: Time
- Y-axis: Story points
- Group by: Status

---

## Metrics to Track

### Velocity
- Story points completed per week
- Track using custom field

### Cycle Time
- Time from "In Progress" → "Done"
- Monitor via GitHub Insights

### WIP Limit
- Max 3 issues per developer in "In Progress"
- Review weekly

---

## Quick Start Commands

### Using GitHub CLI

**Create issue:**
```bash
gh issue create \
  --title "Implement CreateTutorUseCase" \
  --label "P1-high,application,tutor,use-case" \
  --milestone "Phase 2: Tutor"
```

**List issues:**
```bash
gh issue list --label "P1-high"
```

**Create PR:**
```bash
gh pr create --title "feat: add CreateTutorUseCase (#10)" \
             --body "Closes #10"
```

---

## Best Practices

### 1. Small, Focused Issues

Each issue should be completable in 1-3 days (1-5 story points)

### 2. Clear Acceptance Criteria

Every issue needs testable criteria:

```markdown
**Acceptance Criteria:**
- [ ] Use case implements execute() method
- [ ] Unit tests achieve 100% coverage
- [ ] Code is formatted (Spotless)
```

### 3. Link Everything

- Issues → PRs (use "Closes #10")
- Child issues → Parent epic
- Dependencies documented

### 4. Update Regularly

- Move cards when status changes
- Comment on progress
- Close issues when done

---

## Next Steps

1. **Create GitHub Project** - Follow Step 1 above
2. **Set up labels** - Run label creation commands
3. **Configure milestones** - One per phase
4. **Create first issues** - Start with Phase 0
5. **Begin development** - Pick and assign issues

---

## Further Reading

- [Implementation Roadmap](implementation-roadmap) - All 34 issues detailed
- [Contributing Guide](contributing) - Development guidelines
- [GitHub Projects Documentation](https://docs.github.com/en/issues/planning-and-tracking-with-projects)

