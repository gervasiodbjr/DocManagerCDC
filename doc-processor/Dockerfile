### Stage 1: Build
FROM maven:3.9-sapmachine-17 AS build
WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

### Stage 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /usr/src/app
COPY --from=build /usr/src/app/target/quarkus-app/ /usr/src/app/
CMD ["java", "-jar", "/usr/src/app/quarkus-run.jar"]
