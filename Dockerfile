

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the generated jar from the build stage
# Spring Boot's bootJar output usually lands in build/libs/*.jar
COPY build/libs/*.jar /app/app.jar

# Optional: create a non-root user for better security
# RUN addgroup -S app && adduser -S app -G app
# USER app

# Expose the application port (adjust if different)
EXPOSE 8080

# Optional: pass JVM flags via JAVA_OPTS
# HEALTHCHECK is optional; uncomment if you expose actuator
# HEALTHCHECK --interval=30s --timeout=5s CMD wget -qO- http://localhost:8080/actuator/health | grep '"status":"UP"' || exit 1

ENTRYPOINT ["java","-jar", "/app/app.jar"]
