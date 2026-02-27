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

# Help (default)
.PHONY: help
help:
	@echo ""
	@echo "🧠 PetWise Diagram Makefile"
	@echo ""
	@echo "Usage:"
	@echo "  make diagrams          Render all .puml diagrams under docs/ to .png"
	@echo "  make all               Same as 'make diagrams'"
	@echo "  make clean             Delete all generated .png files under docs/"
	@echo "  make <path>.png        Render a single diagram (side-by-side)"
	@echo "  make publish           Copy generated .png files into docs/assets/diagrams preserving subfolders"
	@echo "  make openapi           Generate docs/api/openapi.yaml from annotated controllers"
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
