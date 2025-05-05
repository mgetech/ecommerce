# Stage 1: Build
FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
COPY --from=builder /app/build/libs/*.jar /app/user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
