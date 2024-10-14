FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/user-services-0.0.1-SNAPSHOT.jar app/user-services.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "user-services.jar" ]