# =====================================================================
# Multi-stage build for the MyOffice Spring Boot backend.
# Stage 1: build the fat jar with Maven (Java 17).
# Stage 2: minimal JRE runtime.
# =====================================================================

# ---- Stage 1: build ----
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

# Pre-populate the OnlyOffice docs-integration-sdk local maven repo entry.
# The project ships the SDK under src/main/resources/sdk/ in maven layout.
COPY src/main/resources/sdk /root/.m2/repository

# Copy the rest of the source and build.
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src ./src

RUN mvn -B -Dmaven.test.skip=true clean package

# ---- Stage 2: runtime ----
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the executable jar (Spring Boot produces a single fat jar via the
# spring-boot-maven-plugin "repackage" goal which is bound by default).
COPY --from=build /workspace/target/myoffice-0.0.1.jar /app/app.jar

# Persistent uploaded / generated files live under /app/files (working dir).
RUN mkdir -p /app/files

EXPOSE 80

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
