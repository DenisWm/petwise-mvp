plugins {
    alias(libs.plugins.java.library.convention)
}

dependencies {
    implementation(libs.slf4j.api)

    testImplementation(libs.assertj.core)
}
