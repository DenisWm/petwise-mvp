# Makefile - PetWise diagrams

# Default: show help (so "make" alone is friendly)
.DEFAULT_GOAL := help

# Docker-based PlantUML runner
PLANTUML := docker run --rm -v $(PWD):/workspace -w /workspace plantuml/plantuml

PUML_FILES := $(shell find docs -name '*.puml')
PNG_FILES  := $(PUML_FILES:.puml=.png)

.PHONY: diagrams all
diagrams all: $(PNG_FILES)

%.png: %.puml
	@echo "📐 Rendering $< -> $@"
	@$(PLANTUML) -tpng $<

# Remove all generated PNGs
.PHONY: clean
clean:
	@echo "🧹 Removing generated PNG diagrams..."
	@find docs -name '*.png' -delete

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
	@echo ""
	@echo "Examples:"
	@echo "  make diagrams"
	@echo "  make docs/architecture/c4/c4-context.png"
	@echo "  make docs/architecture/sequences/uc03-create-appointment.png"
	@echo ""
	@echo "Prerequisites:"
	@echo "  - Docker installed and 'plantuml/plantuml' image available (pulled automatically)."
	@echo ""
