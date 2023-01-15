<img src="https://raw.githubusercontent.com/CryptoSingh1337/vidflow-client/master/static/v.png" align="right" height="150" width="132" />

# VidFlow

An open source content sharing platform (similar to YouTube).

Preview: [https://www.youtube.com/watch?v=WJZyM-X8wAo](https://www.youtube.com/watch?v=WJZyM-X8wAo)

Frontend repo: [https://github.com/CryptoSingh1337/vidflow-client](https://github.com/CryptoSingh1337/vidflow-client)

[![Issues Closed](https://img.shields.io/github/issues-closed/CryptoSingh1337/vidflow-backend?color=red)](https://github.com/CryptoSingh1337/vidflow-backend/issues?q=is%3Aissue+is%3Aclosed)
[![Issues Open](https://img.shields.io/github/issues/CryptoSingh1337/vidflow-backend?color=green)](https://github.com/CryptoSingh1337/vidflow-backend/issues)
[![PRs Open](https://img.shields.io/github/issues-pr/CryptoSingh1337/vidflow-backend)](https://github.com/CryptoSingh1337/vidflow-backend/pulls)
![Last Commit](https://img.shields.io/github/last-commit/CryptoSingh1337/vidflow-backend?color=informational)
![PRs Welcome](https://img.shields.io/badge/prs-welcome-ff69b4)
[![wakatime](https://wakatime.com/badge/github/CryptoSingh1337/vidflow-backend.svg)](https://wakatime.com/badge/github/CryptoSingh1337/vidflow-backend)

## Features

- JWT token based user authentication and authorization with refresh token.
- Used pagination and sorting from server side.
- Stores videos on Microsoft azure blob storage.
- User can watch/explore videos without authentication and can upload video with the video metadata like title etc.
- User can post, edit, delete his/her comment.
- User can subscribe/unsubscribe to a channel and will receive notifications for videos (if subscribed).
- User can like/dislike, share a video (copies link to the clipboard).
- Increase views asynchronously if the user watch that video.

## Data model

![application-model](https://user-images.githubusercontent.com/56120837/150513856-e0672393-8aa2-44d2-a3a8-d134633c3e2b.png)

## Application architecture
![vidflow-architecture](https://user-images.githubusercontent.com/56120837/150522748-c66c8c7d-4f24-4688-b904-b6db06605016.png)

## Screenshots

#### Light theme
![light-theme](https://user-images.githubusercontent.com/56120837/150553448-6b781bd1-2930-4216-987b-b3ebb4eee510.png)

#### Landing page
![landing-page](https://user-images.githubusercontent.com/56120837/150551742-b3b301d4-cf4c-4e76-93cd-761ec5552754.png)

#### Landing page after login
![landing-page-after-login](https://user-images.githubusercontent.com/56120837/150552598-4ae46998-0a09-47a0-9a11-872f863df4b3.png)

#### Login/Signup page
![login/signup](https://user-images.githubusercontent.com/56120837/150552011-d01bd689-8650-41d8-9469-1406588fee4a.png)

#### Signup page
![signup](https://user-images.githubusercontent.com/56120837/150552140-82195317-2b7c-47e7-b28b-fd3fef448526.png)

#### Watch video page
![watch-video-page](https://user-images.githubusercontent.com/56120837/150552312-c842c44f-c138-4148-b36a-6bd1d6722dbf.png)

#### Video footer section
![video-footer-section](https://user-images.githubusercontent.com/56120837/150552469-ca3800ea-7994-49a1-8597-c32a5792bfbc.png)

#### Upload video page

##### Upload video
![upload-video-page](https://user-images.githubusercontent.com/56120837/150553809-e20d26d2-a774-4bcb-b23a-0688dde4a62b.png)

##### Add video metadata
![image](https://user-images.githubusercontent.com/56120837/150554236-2252ec9e-8afe-4dbc-b4d3-076283d48e91.png)

#### Channel page
![channel-page](https://user-images.githubusercontent.com/56120837/150552750-6d3247a6-7e6c-45c9-acb4-14e944bf3d67.png)

#### History page
![history-page](https://user-images.githubusercontent.com/56120837/150552900-db191775-59a0-4593-b548-a0ed7a1abd85.png)

#### Search page
![search-page](https://user-images.githubusercontent.com/56120837/150553052-e9112771-5267-45d1-8ad1-6b909acb12d7.png)

#### Your videos page
![your-videos-page](https://user-images.githubusercontent.com/56120837/150553232-33232171-8389-4d03-878c-58f7390ec4f5.png)

## TODO

- [x] Like/dislike video functionality.
- [ ] Forgot password via email.
- [ ] Notifications functionality using redis as data store.
- [ ] Clear watch history after 21 days using Scheduling Tasks.

## Environment variables

Development environment:
```bash
AZURE_STORAGE_CONNECTION_STRING
BLOB_CONTAINER_NAME
S3_ACCESS_KEY
S3_SECRET_KEY
CLOUD_FRONT_BASE
```

Production environment:
```bash
JWT_ACCESS_TOKEN_SECRET
JWT_REFRESH_TOKEN_SECRET
MONGO_URL
AZURE_STORAGE_CONNECTION_STRING
BLOB_CONTAINER_NAME
S3_ACCESS_KEY
S3_SECRET_KEY
CLOUD_FRONT_BASE
```

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

## AWS setup
1. Create a ec2 instance.
2. Add the following inbound rules in the security group:

| Type       | Protocol | Port  | Source    |
|------------|----------|-------|-----------|
| Custom TCP | TCP      | 27017 | 0.0.0.0/0 |
| HTTP       | TCP      | 80    | 0.0.0.0/0 |
| HTTPS      | TCP      | 443   | 0.0.0.0/0 |

3. Create a target group with the following configuration:

| Config                | Value       |
|-----------------------|-------------|
| Target type           | Instances   |
| Target group name     | vidflow     |
| Protocol              | HTTP        |
| Port                  | 80          |
| Protocol version      | HTTP1       |
| Health check protocol | HTTP        |
| Health check path     | /index.html |

4. Register target and select the ec2 instance.
5. Request Certificate in Certificate manager and add the provided record in the advance DNS setting.
6. Create a load balancer with following configuration:

| Config             | Value            |
|--------------------|------------------|
| Load balancer type | Application      |
| Load balancer name | vidflow          |
| Scheme             | Internet-facing  |
| IP address type    | IPv4             |

7. In Route 53, create a hosted zone with the domain name.
8. Change the default name server with the aws name server in the domain name provider portal.
9. Create the following records:

| Record name       |  Type  | Routing | Value                    |
|-------------------|:------:|---------|--------------------------|
| [domain-name]     |   A    | Simple  | [load-balancer-dns-name] |
| www.[domain-name] | CNAME  | Simple  | [domain-name]            |

10. Create a bucket with default configuration with the name `vidflow`.
11. Create a cloudfront distribution with the following configuration:

| Config                  | Value                  |
|-------------------------|------------------------|
| Origin domain           | Amazon s3              |
| Enable Origin shield    | No                     |
| Viewer protocol policy  | Redirect HTTP to HTTPS |
| Allowed HTTP methods    | GET, HEAD              |
| Restrict viewer access  | No                     |

## Azure setup

Create a microsoft storage account and copy the connection string.

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
    - AWS

- **Server**:
  - Java
  - Spring Boot
  - Spring Data Mongo
  - Spring Data Redis
  - Spring Security
  - Microsoft Azure Blob Storage
  - AWS Simple Storage Service
  - **Deployment**:
    - AWS
