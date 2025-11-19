plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    java
    id("petwise.lint-conventions")
    id("petwise.jacoco-conventions")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
