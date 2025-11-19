plugins {
    `java-library`
    id("petwise.lint-conventions")
    id("petwise.jacoco-conventions")
}

dependencies {
    val libs = versionCatalogs.named("libs")

    testImplementation(libs.findLibrary("junit-jupiter").get())
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}


tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
