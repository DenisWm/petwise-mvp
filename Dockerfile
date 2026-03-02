FROM gradle:8.14-jdk21 AS build

ARG APP_VERSION=0.0.0

WORKDIR /app

COPY settings.gradle.kts build.gradle.kts gradle.properties /app/
COPY gradle/libs.versions.toml /app/gradle/
COPY domain/build.gradle.kts domain/
COPY application/build.gradle.kts application/
COPY infrastructure/build.gradle.kts infrastructure/
COPY build-logic/build.gradle.kts build-logic/settings.gradle.kts build-logic/

COPY domain/src /app/domain/src
COPY application/src /app/application/src
COPY infrastructure/src /app/infrastructure/src
COPY build-logic/src /app/build-logic/src

RUN gradle bootJar --no-daemon -Prelease.forceVersion=${APP_VERSION}

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/infrastructure/build/libs/petwise-application.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]