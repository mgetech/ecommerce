# 🛍️ Cloud-Native E-Commerce Microservice Architecture

[![CI/CD](https://github.com/mgetech/ecommerce/actions/workflows/deploy-user-service.yml/badge.svg)](https://github.com/mgetech/ecommerce/actions)
[![CI/CD](https://github.com/mgetech/ecommerce/actions/workflows/deploy-product-service.yml/badge.svg)](https://github.com/mgetech/ecommerce/actions)
[![CI/CD](https://github.com/mgetech/ecommerce/actions/workflows/deploy-order-service.yml/badge.svg)](https://github.com/mgetech/ecommerce/actions)
[![User Service Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=mgetech_ecommerce_user&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mgetech_ecommerce_user)
[![Product Service Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=mgetech_ecommerce_product&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mgetech_ecommerce_product)
[![Order Service Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=mgetech_ecommerce_order&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mgetech_ecommerce_order)

---

## 📦 Overview

This repository contains a **cloud-native e-commerce system** built using a microservices architecture. It’s designed as a professional-grade portfolio project to demonstrate backend and DevOps engineering skills, with CI/CD automation and deployment to Google Cloud Run.

Each microservice is containerized and runs in isolation with its own PostgreSQL database, orchestrated via Docker Compose.

---

## 🧰 Tech Stack

| Layer           | Technologies                                         |
|----------------|------------------------------------------------------|
| Language        | Java 21, Spring Boot                                 |
| Build Tool      | Gradle                                               |
| Containerization| Docker, Docker Compose                               |
| Database        | PostgreSQL (one per service)                         |
| CI/CD           | GitHub Actions, deployed to Google Cloud Run         |
| API Docs         | Swagger UI (SpringDoc OpenAPI)                       |
| Code Quality    | SonarCloud (active), Checkstyle / Spotless (planned) |


## 🧩 Microservices

| Service         | Description                               |
|-----------------|-------------------------------------------|
| **User Service**    | Manages user registration and data        |
| **Product Service** | Handles product catalog and inventory     |
| **Order Service**   | Manages order creation and tracking       |

Each service is fully decoupled and follows the [database-per-service](https://microservices.io/patterns/data/database-per-service.html) pattern.

---

## 🚀 Features

- ✅ Modular microservice design
- ✅ Each service containerized with Docker
- ✅ Local orchestration via Docker Compose
- ✅ CI/CD pipeline with automated testing, build, and deploy
- ✅ Live endpoints planned for public testing
- 🛠️ Continuously evolving with more integrations and features

---





## 🌐 Live Access


This project is deployed on Google Cloud Run, and each microservice is publicly accessible. You can interact with the APIs directly or explore them via Swagger UI.

---

### 🔗 Service URLs

| Service         | Base URL                                                                 | Swagger UI           |
|-----------------|--------------------------------------------------------------------------|----------------------|
| 🧑‍ User Service    | `https://user-service-716746262210.europe-west3.run.app`               | [User API Docs](https://user-service-716746262210.europe-west3.run.app/swagger-ui.html)    |
| 📦 Product Service | `https://product-service-716746262210.europe-west3.run.app`            | [Product API Docs](https://product-service-716746262210.europe-west3.run.app/swagger-ui.html) |
| 🛒 Order Service   | `https://order-service-716746262210.europe-west3.run.app`              | [Order API Docs](https://order-service-716746262210.europe-west3.run.app/swagger-ui.html)   |


### 🛒 Create an Order (End-to-End Example)

Below is the most important flow — placing an order — which interacts with all three services:
```
POST https://order-service-716746262210.europe-west3.run.app/orders
Content-Type: application/json

{
  "userId": 1,
  "productId": 2,
  "quantity": 3
}
```
✅ This will create an order that references existing user and product IDs.

---
## To run locally:
```bash
git clone https://github.com/mgetech/ecommerce.git
cd ecommerce/app
docker compose up --build
