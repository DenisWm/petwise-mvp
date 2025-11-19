plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.named("check") {
    dependsOn("jacocoTestReport")
}
