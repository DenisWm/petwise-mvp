plugins {
    alias(libs.plugins.java.library.convention)
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.slf4j.api)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
}
