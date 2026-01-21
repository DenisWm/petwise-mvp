plugins {
    jacoco
    id("jacoco-report-aggregation")
}

jacoco {
    toolVersion = "0.8.11"
}

val aggregatedModules = project.rootProject.subprojects
    .filter { it.tasks.findByName("test") != null }
    .map { it.path }

dependencies {
    aggregatedModules.forEach { modulePath ->
        jacocoAggregation(project(modulePath))
    }
}

tasks.named("testCodeCoverageReport", JacocoReport::class.java) {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("$rootDir/build/reports/jacoco/test/jacocoTestReport.xml"))

        html.required.set(true)
        html.outputLocation.set(file("$rootDir/build/reports/jacoco/test/"))
    }
}

tasks.named("jacocoTestReport") {
    dependsOn(tasks.named("testCodeCoverageReport", JacocoReport::class.java))
}

tasks.register("cleanJacocoReports", org.gradle.api.tasks.Delete::class.java) {
    delete(file("$rootDir/build/reports/jacoco"))
}

tasks.named("clean") {
    dependsOn("cleanJacocoReports")
}

tasks.named("test") {
    dependsOn("jacocoTestReport")
}
