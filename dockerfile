FROM openjdk:11-jdk-slim AS build
WORKDIR /opt/project
COPY gradle             /opt/project/gradle
COPY gradlew            /opt/project/
COPY build.gradle       /opt/project/
COPY settings.gradle    /opt/project/
COPY src                /opt/project/src
RUN chmod +x /opt/project/gradlew
RUN ./gradlew bootJar
