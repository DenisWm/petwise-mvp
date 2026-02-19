plugins {
    alias(libs.plugins.java.library.convention)
}

dependencies {
    implementation(project(":domain"))

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
}
