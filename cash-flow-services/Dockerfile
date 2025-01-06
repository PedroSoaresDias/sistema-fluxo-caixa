FROM maven:3.9.9-eclipse-temurin-21-alpine as builder

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder target/*.jar cash-flow-services.jar

EXPOSE 8082

CMD [ "java", "-jar", "cash-flow-services.jar" ]