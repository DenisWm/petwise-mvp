plugins {
    alias(libs.plugins.java.library.convention)
}

dependencies {
    implementation(project(":domain"))
}
