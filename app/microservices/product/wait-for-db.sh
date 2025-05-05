#!/bin/sh

echo "Waiting for database at $PRODUCT_DB_HOST:$PRODUCT_DB_PORT..."

until pg_isready -h "$PRODUCT_DB_HOST" -p "$PRODUCT_DB_PORT" -U "$SPRING_DATASOURCE_USERNAME"; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 2
done

echo "Postgres is up - starting the application"
exec java -jar /app/product-service.jar
