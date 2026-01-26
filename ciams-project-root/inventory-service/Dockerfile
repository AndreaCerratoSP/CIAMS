

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the generated jar from the build stage
# Spring Boot's bootJar output usually lands in build/libs/*.jar
COPY build/libs/*.jar /app/app.jar

# Expose the application port (adjust if different)
EXPOSE 8080

ENTRYPOINT ["java","-jar", "/app/app.jar"]
