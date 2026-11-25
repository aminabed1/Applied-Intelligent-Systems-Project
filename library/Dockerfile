FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ADD target/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]