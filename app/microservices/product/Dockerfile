# Stage 1: Build
FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

# Install PostgreSQL client for pg_isready
RUN apk add --no-cache postgresql-client

# Copy built JAR and wait script
COPY --from=builder /app/build/libs/*.jar /app/product-service.jar
#COPY wait-for-db.sh /app/wait-for-db.sh

# Make script executable
#RUN chmod +x /app/wait-for-db.sh
# For cloud deployment
ENV SPRING_PROFILES_ACTIVE=cloud
EXPOSE 8080
WORKDIR /app

#ENTRYPOINT ["sh", "/app/wait-for-db.sh"]
ENTRYPOINT ["java","-jar","/app/product-service.jar"]
