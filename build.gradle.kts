/**
 * Root build file - configures axion-release-plugin for automatic
 * semantic-version tagging based on git history.
 *
 * The plugin reads existing git tags (e.g. 1.0.1, 1.0.2) and computes
 * the next version automatically. It also sets project.version for all
 * subprojects.
 *
 * Usage:
 *   ./gradlew currentVersion          - print the resolved version
 *   ./gradlew release                 - tag + push the next version
 *   ./gradlew release -Prelease.forceVersion=2.0.0  - force a specific version
 */

plugins {
    alias(libs.plugins.axion.release)
}

scmVersion {
    // Tag format: plain semver without prefix (1.0.1 instead of v1.0.1)
    tag {
        prefix.set("")
        versionSeparator.set("")
    }
}

// Set the root project version from axion
version = scmVersion.version

// Propagate the resolved version to every subproject
subprojects {
    version = rootProject.version
}
