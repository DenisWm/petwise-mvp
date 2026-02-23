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

    analyzers {
        // Sonatype OSS Index fails on build-time deps (checkstyle, antlr, etc.) — use NVD only
        ossIndex.enabled = false

        // Not a Node.js project
        nodeAudit.enabled = false
        nodePackage.enabled = false

        // Not applicable to this project
        assemblyEnabled = false
        nuspecEnabled = false
        nugetconfEnabled = false
        pyDistributionEnabled = false
        pyPackageEnabled = false
        rubygemsEnabled = false
        golangDepEnabled = false
        golangModEnabled = false
        cocoapodsEnabled = false
        swiftPackageResolvedEnabled = false
        dartEnabled = false
    }
}

