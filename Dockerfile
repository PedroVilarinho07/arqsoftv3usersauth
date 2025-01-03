# Use a Maven image with JDK installed
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY ../pom.xml .
RUN mvn dependency:go-offline

COPY ../src ./src

RUN mvn package

FROM openjdk:21-jdk-slim

WORKDIR /app

ENV SERVER_PORT=9192
EXPOSE 9192

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]