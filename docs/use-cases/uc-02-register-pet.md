---
layout: default
title: UC-02 Register Pet
parent: Use Cases
nav_order: 2
---

# UC-02 – Register Pet
{: .no_toc }

Allow an attendant to register a new pet and associate it with an existing tutor.
{: .fs-6 .fw-300 }

---

{% include_relative specs/uc-02-register-pet.md %}

---

## Sequence Diagram

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc02-create-pet.png" alt="UC-02 Sequence Diagram" />
</div>

{: .note }
> If the diagram is not visible, run `make diagrams publish` from the project root to render PlantUML sources.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc02-create-pet.puml)
