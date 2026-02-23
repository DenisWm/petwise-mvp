plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("petwise.lint-conventions")
    id("petwise.jacoco-conventions")
}

tasks.bootJar {
    archiveFileName.set("petwise-application.jar")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}