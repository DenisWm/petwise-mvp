rootProject.name = "petwise"
include("domain")
include("application")
include("infrastructure")

includeBuild("build-logic")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}