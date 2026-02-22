
plugins {
    id("com.diffplug.spotless")
    id("petwise.owasp-dependency-check-conventions")
    checkstyle
    pmd

}
checkstyle {
    toolVersion = "10.17.0"
    isIgnoreFailures = false
    configFile = rootProject.file("build-logic/config/checkstyle/checkstyle.xml")
    configProperties["org.checkstyle.sun.suppressionfilter.config"] =
        rootProject.file(
            "build-logic/config/checkstyle/checkstyle-suppressions.xml"
        ).absolutePath
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = true
        html.required = true
    }
}

spotless {
    java {
        googleJavaFormat().aosp()
        leadingTabsToSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        leadingTabsToSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }

}

pmd {
    toolVersion = "7.13.0"
    ruleSetFiles = files(project(":").file("build-logic/config/pmd/custom-rules.xml"))
    isIgnoreFailures = false
}

