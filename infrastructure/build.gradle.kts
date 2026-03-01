import org.gradle.jvm.tasks.Jar

plugins {
    alias(libs.plugins.spring.boot.app.convention)
    alias(libs.plugins.springdoc.openapi)
}


dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation(libs.bundles.spring.boot.starter)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.undertow)
    implementation(libs.spring.boot.starter.web)
    { exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat") }
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.jackson.module.afterburner)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.logstash.logback.encoder)

    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.h2)

}

openApi {
    apiDocsUrl.set("http://localhost:8080/api/v3/api-docs.yaml")
    outputDir.set(layout.projectDirectory.dir("../docs/api"))
    outputFileName.set("openapi.yaml")
//    forkProperties.set("-Dspring.profiles.active=test-integration")
}

tasks.named("forkedSpringBootRun") {
    val domainJar = project(":domain").tasks.named<Jar>("jar").flatMap { it.archiveFile }
    val applicationJar = project(":application").tasks.named<Jar>("jar").flatMap { it.archiveFile }
    inputs.file(domainJar)
    inputs.file(applicationJar)
}
