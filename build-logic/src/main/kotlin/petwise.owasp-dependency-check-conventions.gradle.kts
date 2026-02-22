plugins {
    id("org.owasp.dependencycheck")
}

dependencyCheck {
    // Fail the build if any dependency has a CVSS score >= 7 (HIGH or CRITICAL)
    failBuildOnCVSS = 7.0f

    // NVD API key — provided via environment variable / CI secret
    nvd.apiKey = System.getenv("NVD_API_KEY") ?: ""

    // Output formats: HTML for human review + XML for tooling
    formats = listOf("HTML", "XML")

    // Place reports under each subproject's standard build/reports dir
    outputDirectory = "${project.layout.buildDirectory.get()}/reports/dependency-check"

    suppressionFiles = listOf("${rootProject.projectDir}/build-logic/config/owasp/suppressions.xml")
}

