FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder target/*.jar discovery-services.jar

EXPOSE 8761

CMD [ "java", "-jar", "discovery-services.jar" ]