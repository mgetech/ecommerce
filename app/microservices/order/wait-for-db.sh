#!/bin/sh

echo "Waiting for database at $ORDER_DB_HOST:$ORDER_DB_PORT..."

until pg_isready -h "$ORDER_DB_HOST" -p "$ORDER_DB_PORT" -U "$SPRING_DATASOURCE_USERNAME"; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 2
done

echo "Postgres is up - starting the application"
exec java -jar /app/order-service.jar
