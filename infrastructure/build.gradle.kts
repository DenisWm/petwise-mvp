plugins {
    alias(libs.plugins.spring.boot.app.convention)
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation(libs.bundles.spring.boot.starter)
    implementation(libs.spring.boot.starter.undertow)
    implementation(libs.spring.boot.starter.web)
    { exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat") }
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.jackson.module.afterburner)
//    implementation(libs.flyway.core)
//    implementation(libs.flyway.database.postgresql)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.h2)
}
