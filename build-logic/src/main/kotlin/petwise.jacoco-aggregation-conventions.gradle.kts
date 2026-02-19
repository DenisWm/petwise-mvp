plugins {
    base
    jacoco
}

jacoco {
    toolVersion = "0.8.11"
}

// Register the aggregated coverage report task manually
val testCodeCoverageReport by tasks.registering(JacocoReport::class) {
    group = "verification"
    description = "Generates aggregated JaCoCo coverage report for all modules"

    // Collect execution data from all subprojects
    executionData.setFrom(fileTree(rootProject.projectDir) {
        include("**/build/jacoco/test.exec")
    })

    reports {
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml"))

        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/test/"))
    }
}

// Configure source and class directories after subprojects are evaluated
gradle.projectsEvaluated {
    val srcDirs = mutableListOf<File>()
    val classDirs = mutableListOf<FileCollection>()

    subprojects.forEach { subproject ->
        subproject.plugins.withId("java") {
            val sourceSets = subproject.extensions.getByType(SourceSetContainer::class.java)
            sourceSets.getByName("main").let { main ->
                srcDirs.addAll(main.allSource.srcDirs)
                classDirs.add(main.output.classesDirs)
            }
        }
    }

    tasks.named<JacocoReport>("testCodeCoverageReport") {
        sourceDirectories.setFrom(srcDirs)
        classDirectories.setFrom(classDirs)
    }
}

tasks.register("jacocoAggregatedReport") {
    group = "verification"
    description = "Generates aggregated JaCoCo coverage report for all modules"
    dependsOn(testCodeCoverageReport)
}

tasks.register<Delete>("cleanJacocoReports") {
    delete(layout.buildDirectory.dir("reports/jacoco"))
}

tasks.named("clean") {
    dependsOn("cleanJacocoReports")
}

tasks.named("check") {
    dependsOn("jacocoAggregatedReport")
}

