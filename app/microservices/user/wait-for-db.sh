#!/bin/sh

echo "Waiting for database at $USER_DB_HOST:$USER_DB_PORT..."

until pg_isready -h "$USER_DB_HOST" -p "$USER_DB_PORT" -U "$SPRING_DATASOURCE_USERNAME"; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 2
done

echo "Postgres is up - starting the application"
exec java -jar /app/user-service.jar
