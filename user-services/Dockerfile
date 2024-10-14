FROM maven:3.9.9-eclipse-temurin-21-alpine as builder

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN mkdir /app

WORKDIR /app

COPY --from=builder target/*.jar /app/user-services.jar

EXPOSE 8080

CMD [ "java", "-jar", "/app/user-services.jar" ]