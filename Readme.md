<img src="https://raw.githubusercontent.com/CryptoSingh1337/vidflow-client/master/static/v.png" align="right" />

# VidFlow

An open source content sharing platform (YouTube clone). This application will be highly
scalable because I am following the micro-service architecture in backend and the [frontend](https://github.com/CryptoSingh1337/vidflow-client) is created
using [nuxt](https://github.com/nuxt/nuxt.js).

[![Issues Closed](https://img.shields.io/github/issues-closed/CryptoSingh1337/vidflow-backend?color=red)](https://github.com/CryptoSingh1337/vidflow-backend/issues?q=is%3Aissue+is%3Aclosed)
[![Issues Open](https://img.shields.io/github/issues/CryptoSingh1337/vidflow-backend?color=green)](https://github.com/CryptoSingh1337/vidflow-backend/issues)
[![PRs Open](https://img.shields.io/github/issues-pr/CryptoSingh1337/vidflow-backend)](https://github.com/CryptoSingh1337/vidflow-backend/pulls)
![Last Commit](https://img.shields.io/github/last-commit/CryptoSingh1337/vidflow-backend?color=informational)
![PRs Welcome](https://img.shields.io/badge/prs-welcome-ff69b4)

## Demo

[https://vidflow.vercel.app](https://vidflow.vercel.app)

## Features

- JWT token based user authentication and authorization with refresh token.
- Centralized Configuration for each service.
- Api Gateway for single entry point and for authentication.
- Message broker (RabbitMQ) for Notification service.
- Used pagination and sorting from server side.
- Stores videos on Microsoft Azure Storage service.
- User can watch/explore videos without authentication and can upload, delete, update thumbnail, title, description of a video.
- User can subscribe to a channel and will receive notifications for videos.
- User can like/dislike, share a video.

## Run Locally

Clone the project

```bash
  git clone https://github.com/CryptoSingh1337/vidflow-backend.git
```

Go to the project directory

```bash
  cd vidflow-backend
```

> **_Note_**: You have to run all these applications one by one. First run Discovery service, Config Server, ApiGateway and then other services

Run verfiy goal

```bash
  ./mvnw clean verify
```

Run Spring Boot application (default port: 8080 or mentioned in `application.properties`)

```bash
  ./mvnw spring-boot:run
```

## Tech Stack

- **Client**:

  - JavaScript
  - Vue
  - Nuxt
  - Vuex
  - Vuex Router
  - Vuetify
  - **Deployment**:
    - Vercel

- **Server**:
  - Java
  - Spring Boot
  - **Cloud**:
    - Spring Cloud Netflix Eureka Server and Client
    - Spring Cloud Gateway
    - Spring Cloud Bus
    - Spring Cloud Config Server
    - Spring Cloud Open Feign
    - Spring Cloud Resilience4j
    - Spring Cloud Zipkin
    - Spring Cloud Sleuth
    - Amazon S3
  - **Deployment**:
    - Amazon AWS
