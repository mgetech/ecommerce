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
COPY --from=builder /app/build/libs/*.jar /app/order-service.jar
COPY wait-for-db.sh /app/wait-for-db.sh

# Make script executable (important on Windows)
RUN chmod +x /app/wait-for-db.sh

WORKDIR /app
EXPOSE 8081

ENTRYPOINT ["sh", "/app/wait-for-db.sh"]
