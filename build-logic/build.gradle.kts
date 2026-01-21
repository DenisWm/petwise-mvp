plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.spring.boot.gradle)
    implementation(libs.io.spring.dependency.management.gradle)
    implementation(libs.spotless.gradle)
}
