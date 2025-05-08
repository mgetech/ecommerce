# Ecommerce Microservices Project

This is a microservice-based ecommerce backend built with Spring Boot and Docker. It includes three core services:

- **User Service** – Manages user data and registration.
- **Product Service** – Handles product catalog and inventory.
- **Order Service** – Manages order creation and tracking.

Each service has its own PostgreSQL database and is containerized using Docker. The entire system is orchestrated using Docker Compose.

## Features

- RESTful APIs for user, product, and order management
- Fully containerized with Docker & Docker Compose
- CI/CD pipeline with GitHub Actions
- Deployed on **Amazon EC2**

## Example Request

You can test an example endpoint:

```http
GET http://ec2-13-48-137-243.eu-north-1.compute.amazonaws.com:8081/users/4
Accept: application/json
```


## Getting Started

To run locally:
```bash
cd app
docker compose up --build
