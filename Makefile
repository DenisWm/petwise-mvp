# Makefile - PetWise diagrams

# Default: show help (so "make" alone is friendly)
.DEFAULT_GOAL := help

# Docker-based PlantUML runner
PLANTUML := docker run --rm -v $(PWD):/workspace -w /workspace plantuml/plantuml

PUML_FILES := $(shell find docs -name '*.puml')
PNG_FILES  := $(PUML_FILES:.puml=.png)

# Destination directory for published diagrams (preserves subfolders under docs/)
PUBLISH_DIR := docs/assets/diagrams

.PHONY: diagrams all
diagrams all: $(PNG_FILES)

%.png: %.puml
	@echo "📐 Rendering $< -> $@"
	@$(PLANTUML) -tpng $<

# Publish generated PNGs into $(PUBLISH_DIR), keeping the same subfolder layout
.PHONY: publish
publish: $(PNG_FILES)
	@echo "📤 Publishing generated diagrams to $(PUBLISH_DIR)"
	@for p in $(PUML_FILES); do \
		src=$${p%.puml}.png; \
		rel=$${p#docs/}; \
		dest=$(PUBLISH_DIR)/$${rel%.puml}.png; \
		mkdir -p "$$(dirname "$$dest")"; \
		if [ -f "$$src" ]; then \
			echo " -> $$src -> $$dest"; \
			cp "$$src" "$$dest"; \
		else \
			echo "Warning: $$src not found. Run 'make diagrams' first."; \
		fi; \
	done

# Remove all generated PNGs
.PHONY: clean
clean:
	@echo "🧹 Removing generated PNG diagrams (excluding published assets)..."
	# Find PNG files and exclude protected paths. Using -prune with -delete is problematic
	# because -delete implies -depth; instead use ! -path to explicitly exclude.
	@find docs -type f -name '*.png' ! -path 'docs/_site/*' ! -path 'docs/assets/diagrams/*' ! -path 'docs/assets/images/*' -print -delete

.PHONY: clean-dry-run
clean-dry-run:
	@echo "🧹 Dry-run: PNGs that would be removed (excluding published assets and images):"
	@find docs -type f -name '*.png' ! -path 'docs/_site/*' ! -path 'docs/assets/diagrams/*' ! -path 'docs/assets/images/*' -print

# Generate OpenAPI spec from annotated controllers (requires no external DB)
.PHONY: openapi
openapi:
	@echo "📝 Generating OpenAPI specification..."
	@./gradlew :infrastructure:generateOpenApiDocs
	@echo "✅ docs/api/openapi.yaml updated"

# ---------------------------------------------------------------------------
# ERD – Auto-generated Entity-Relationship Diagram (SchemaSpy)
# ---------------------------------------------------------------------------
# Connects to the running PostgreSQL container and generates an interactive
# HTML report with SVG diagrams.  Zero manual maintenance: the ERD always
# reflects the actual database schema (Flyway migrations).
#
# Prerequisites: `make infra-up` (PostgreSQL must be running on localhost:5432)
# ---------------------------------------------------------------------------
ERD_OUTPUT := docs/erd

.PHONY: erd
erd:
	@echo "🗄️  Generating ERD from live database (SchemaSpy)..."
	@mkdir -p $(ERD_OUTPUT)
	@docker run --rm --network host \
		-v $(PWD)/$(ERD_OUTPUT):/output \
		-e SCHEMASPY_OUTPUT=/output \
		schemaspy/schemaspy:latest \
		-t pgsql11 \
		-host localhost -port 5432 \
		-db petwise -u postgres -p postgres \
		-s public \
		-norows
	@echo "✅ ERD generated at $(ERD_OUTPUT)/index.html"
	@echo "   Open in browser: file://$(PWD)/$(ERD_OUTPUT)/index.html"

.PHONY: erd-clean
erd-clean:
	@echo "🧹 Removing generated ERD..."
	@rm -rf $(ERD_OUTPUT)

# ---------------------------------------------------------------------------
# Infrastructure (Docker Compose)
# ---------------------------------------------------------------------------
.PHONY: infra-up
infra-up:
	@echo "🐳 Starting infrastructure (PostgreSQL + Keycloak)..."
	@docker compose up -d db keycloak
	@echo "✅ Keycloak: http://localhost:9080 (admin/admin)"
	@echo "   Realm:    petwise | Client: petwise-api"

.PHONY: infra-down
infra-down:
	@echo "🛑 Stopping all Docker Compose services..."
	@docker compose down

.PHONY: infra-logs
infra-logs:
	@docker compose logs -f keycloak

.PHONY: keycloak-export
keycloak-export:
	@echo "📤 Exporting Keycloak realm to infra/keycloak/..."
	@docker compose exec keycloak /opt/keycloak/bin/kc.sh export \
		--dir /opt/keycloak/data/export --realm petwise
	@docker compose cp keycloak:/opt/keycloak/data/export/petwise-realm.json \
		infra/keycloak/petwise-realm-export.json
	@echo "✅ Exported to infra/keycloak/petwise-realm-export.json"

# Help (default)
.PHONY: help
help:
	@echo ""
	@echo "🧠 PetWise Makefile"
	@echo ""
	@echo "Usage:"
	@echo "  make diagrams          Render all .puml diagrams under docs/ to .png"
	@echo "  make all               Same as 'make diagrams'"
	@echo "  make clean             Delete all generated .png files under docs/"
	@echo "  make <path>.png        Render a single diagram (side-by-side)"
	@echo "  make publish           Copy generated .png files into docs/assets/diagrams preserving subfolders"
	@echo "  make openapi           Generate docs/api/openapi.yaml from annotated controllers"
	@echo ""
	@echo "  make erd               Generate ERD from live PostgreSQL (requires infra-up)"
	@echo "  make erd-clean         Remove generated ERD"
	@echo ""
	@echo "  make infra-up          Start DB + Keycloak (docker compose)"
	@echo "  make infra-down        Stop all Docker Compose services"
	@echo "  make infra-logs        Follow Keycloak container logs"
	@echo "  make keycloak-export   Export current Keycloak realm to infra/keycloak/"
	@echo ""
	@echo "Examples:"
	@echo "  make diagrams"
	@echo "  make docs/architecture/c4/c4-context.png"
	@echo "  make docs/architecture/sequences/uc03-create-appointment.png"
	@echo "  make publish"
	@echo ""
	@echo "Prerequisites:"
	@echo "  - Docker installed and 'plantuml/plantuml' image available (pulled automatically)."
	@echo ""
