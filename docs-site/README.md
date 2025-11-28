# PetWise Documentation Site

This is the Jekyll-based documentation site for PetWise, ready for GitHub Pages deployment.

---

## 🚀 Quick Start (Local Development)

### Prerequisites
- Docker and Docker Compose

### Run Locally

```bash
# Start the Jekyll server
docker-compose up

# The site will be available at:
# http://localhost:4000
```

The server includes **LiveReload** - changes to files will automatically refresh your browser.

---

## 📁 Project Structure

```
docs-site/
├── _config.yml              # Jekyll configuration
├── index.md                 # Home page
├── Gemfile                  # Ruby dependencies
├── docker-compose.yml       # Docker Compose config
│
├── _architecture/           # Architecture docs (collection)
├── _use-cases/              # Use case docs (collection)
├── _api/                    # API docs (collection)
│
├── getting-started.md       # Getting Started guide
├── architecture-deep-dive.md
├── contributing.md
├── build-system.md
├── implementation-roadmap.md
├── github-project-setup.md
├── api-reference.md
│
└── assets/                  # CSS, images, etc.
```

---

## 📝 Adding Documentation

### Create a Top-Level Page

```markdown
---
layout: default
title: My Page
nav_order: 5
---

# My Page Content
```

### Create a Section with Children

**Parent page:**
```markdown
---
layout: default
title: API
nav_order: 6
has_children: true
---

# API Documentation
```

**Child page:**
```markdown
---
layout: default
title: REST Endpoints
parent: API
nav_order: 1
---

# REST Endpoints
```

### Navigation Order

Pages are ordered by `nav_order` in front matter:
- Lower numbers appear first
- Pages without `nav_order` appear last

---

## 🎨 Customization

### Change Colors

Edit `_sass/color_schemes/petwise.scss`:

```scss
$link-color: #0366d6;
$btn-primary-color: #0366d6;
```

### Enable Dark Mode

Edit `_config.yml`:

```yaml
color_scheme: dark  # or petwise
```

### Change Theme Settings

Edit `_config.yml` - see [Just the Docs Configuration](https://just-the-docs.com/docs/configuration/) for all options.

---

## 📤 Deploy to GitHub Pages

### Option 1: GitHub Actions (Recommended)

1. Create `.github/workflows/jekyll.yml`:

```yaml
name: Deploy Jekyll site to Pages

on:
  push:
    branches: ["main"]
  workflow_dispatch:

permissions:
  contents: read
  pages: write
  id-token: write

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/configure-pages@v4
      - uses: actions/jekyll-build-pages@v1
        with:
          source: ./docs-site
          destination: ./_site
      - uses: actions/upload-pages-artifact@v3

  deploy:
    environment:
      name: github-pages
      url: ${{ steps.deployment.outputs.page_url }}
    runs-on: ubuntu-latest
    needs: build
    steps:
      - uses: actions/deploy-pages@v4
        id: deployment
```

2. In GitHub repo settings:
   - Go to **Settings** → **Pages**
   - Source: **GitHub Actions**

3. Push to `main` branch - site will auto-deploy

### Option 2: Deploy from Branch

1. Build the site locally:
   ```bash
   docker-compose run jekyll jekyll build
   ```

2. In GitHub repo settings:
   - Go to **Settings** → **Pages**
   - Source: **Deploy from a branch**
   - Branch: `main`, Folder: `/docs-site`

---

## 🧪 Testing Locally

```bash
# Start the server
docker-compose up

# In another terminal, check the build
docker-compose exec jekyll jekyll build

# Check for broken links (optional, install html-proofer)
docker-compose run jekyll bundle exec htmlproofer ./_site
```

---

## 📝 Writing Documentation

### Add a New Page

Create a markdown file in the root:

```markdown
---
layout: page
title: My Page
permalink: /my-page/
---

# My Page Content

Content goes here...
```

### Add to a Collection

Create a file in the appropriate collection folder:

```markdown
---
title: My Architecture Doc
---

# Content here
```

---

## 🎨 Theme

This site uses the **Minima** theme. To customize:

- [Minima Documentation](https://github.com/jekyll/minima)
- Override layouts by creating files in `_layouts/`
- Override includes by creating files in `_includes/`

---

## 🛠️ Troubleshooting

### Port Already in Use
```bash
docker-compose down
# Change port in docker-compose.yml
```

### Gem Installation Issues
```bash
docker-compose down
docker-compose build --no-cache
docker-compose up
```

### Site Not Updating
- Check for syntax errors in markdown files
- Restart the Jekyll server
- Force rebuild: `docker-compose run jekyll jekyll clean`

---

## 📚 Resources

- [Jekyll Documentation](https://jekyllrb.com/docs/)
- [GitHub Pages Documentation](https://docs.github.com/en/pages)
- [Markdown Guide](https://www.markdownguide.org/)
- [Liquid Template Language](https://shopify.github.io/liquid/)

---

**Ready to publish your documentation!** 🚀

