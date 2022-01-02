<img src="https://raw.githubusercontent.com/CryptoSingh1337/vidflow-client/master/static/v.png" align="right" />

# VidFlow

An open source content sharing platform (YouTube clone).

[![Issues Closed](https://img.shields.io/github/issues-closed/CryptoSingh1337/vidflow-backend?color=red)](https://github.com/CryptoSingh1337/vidflow-backend/issues?q=is%3Aissue+is%3Aclosed)
[![Issues Open](https://img.shields.io/github/issues/CryptoSingh1337/vidflow-backend?color=green)](https://github.com/CryptoSingh1337/vidflow-backend/issues)
[![PRs Open](https://img.shields.io/github/issues-pr/CryptoSingh1337/vidflow-backend)](https://github.com/CryptoSingh1337/vidflow-backend/pulls)
![Last Commit](https://img.shields.io/github/last-commit/CryptoSingh1337/vidflow-backend?color=informational)
![PRs Welcome](https://img.shields.io/badge/prs-welcome-ff69b4)

## Demo

[https://vidflow.vercel.app](https://vidflow.vercel.app)

## Features

- JWT token based user authentication and authorization with refresh token.
- Used pagination and sorting from server side.
- Stores videos on Microsoft azure blob storage.
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
  - Spring Data Mongo
  - Spring Data Redis
  - Spring Security
  - Microsoft Azure Blob Storage
  - **Deployment**:
    - Heroku
