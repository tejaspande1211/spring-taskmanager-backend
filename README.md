# Task Manager Backend (Spring Boot)

This is a backend-only Task Manager API that I built as a final-year
Computer Engineering student to practice real-world backend development
with Java, Spring Boot, MySQL, Redis, RabbitMQ, and AWS EC2.

It's designed to feel like a small production backend: users sign up and
log in with JWT-based authentication, then manage their tasks with
proper CRUD APIs, pagination, search, caching, and async notifications.

------------------------------------------------------------------------

## Why I Built This

I wanted a single project that:

-   Shows I can build a clean, RESTful backend from scratch.
-   Uses technologies that real companies actually use (Spring Boot,
    MySQL, Redis, RabbitMQ, Docker, AWS EC2).
-   Goes beyond simple CRUD and touches authentication, caching,
    messaging, and cloud deployment.

This project is meant to be something I can confidently explain in
interviews and showcase on my resume as a backend-focused fresher.

------------------------------------------------------------------------

## Features

-   User registration and login with JWT authentication.

### Task Management

-   Create, read, update, delete tasks.

-   Status, priority, due date, and attachment URL support.

-   Pagination, sorting, and basic filtering.

-   Redis caching for faster reads of frequently accessed tasks.

-   RabbitMQ-based async notifications when tasks are created (simulated
    email notifications).

-   Centralized error handling and validation.

-   API testing using Postman.

-   Optional deployment on AWS EC2.

------------------------------------------------------------------------

## Tech Stack

### Backend

-   Java 17
-   Spring Boot 3.x (Web, Data JPA, Security, Validation)
-   JWT for authentication
-   MySQL (running via MySQL command line client on `root@localhost`)
-   Redis (via Docker)
-   RabbitMQ (via Docker)

### Tools

-   Maven
-   IntelliJ IDEA
-   Docker Desktop
-   Postman
-   Git & GitHub
-   AWS EC2 (Ubuntu) for deployment

------------------------------------------------------------------------

## Project Architecture (High-Level)

### Spring Boot REST API

-   `AuthController` -- user registration and login.
-   `TaskController` -- all task-related operations (CRUD, pagination,
    filtering).

### Database Layer

-   MySQL for users and tasks (relational, normalized schema).
-   Spring Data JPA repositories.

### Caching Layer

-   Redis for caching selected reads (e.g., getting a task by ID).

### Messaging Layer

-   RabbitMQ for async notifications when a task is created (producer +
    consumer).

------------------------------------------------------------------------

## Request Flow

Client (Postman)\
→ Spring Boot API\
→ MySQL\
→ Redis (for caching)\
→ RabbitMQ (for async notifications)

------------------------------------------------------------------------

# Getting Started (Local Setup)

## 1. Prerequisites

Make sure you have:

-   Java 17 installed and added to PATH\
-   Maven installed (`mvn -v` to verify)\
-   MySQL installed locally (using MySQL command line client on
    `root@localhost`)\
-   Docker Desktop installed and running (for Redis and RabbitMQ)\
-   Postman (or any HTTP client)

------------------------------------------------------------------------

## 2. Clone the Repository

``` bash
git clone https://github.com/tejaspande1211/spring-taskmanager-backend.git
cd spring-taskmanager-backend
```

------------------------------------------------------------------------

## 3. MySQL Setup

Open your MySQL CLI and run:

``` sql
CREATE DATABASE taskmanager;
CREATE USER 'tejas'@'localhost' IDENTIFIED BY 'changeme';
GRANT ALL PRIVILEGES ON taskmanager.* TO 'tejas'@'localhost';
FLUSH PRIVILEGES;
```

Update `application.yml` or `application.properties`:

``` yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/taskmanager
    username: tejas
    password: changeme
    driver-class-name: com.mysql.cj.jdbc.Driver
```

> Note: Change username/password in your own local environment.

------------------------------------------------------------------------

## 4. Start Redis and RabbitMQ with Docker

``` bash
# Redis
docker run -d --name redis-server -p 6379:6379 redis:7-alpine

# RabbitMQ (with management UI)
docker run -d --name rabbitmq-server \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

Redis:\
`localhost:6379`

RabbitMQ UI:\
http://localhost:15672\
Default credentials: `guest / guest`

Make sure your `application.yml` includes:

``` yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379

  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

------------------------------------------------------------------------

## 5. Configure JWT Secret

``` yaml
jwt:
  secret: your-very-long-secret-key-here
  expiration: 86400000
```

Use a strong secret in your actual environment.

------------------------------------------------------------------------

## 6. Build and Run the Application

``` bash
mvn clean package -DskipTests
mvn spring-boot:run
```

Or run the generated JAR:

``` bash
java -jar target/taskmanager-api-0.0.1-SNAPSHOT.jar
```

Application runs at:

    http://localhost:8080

------------------------------------------------------------------------

# API Overview

## Auth

-   `POST /api/auth/register` -- Register a new user\
-   `POST /api/auth/login` -- Log in and receive JWT token

## Tasks (JWT Required)

-   `POST /api/tasks` -- Create task\
-   `GET /api/tasks` -- Get paginated tasks\
-   `GET /api/tasks/{id}` -- Get task by ID\
-   `PUT /api/tasks/{id}` -- Update task\
-   `DELETE /api/tasks/{id}` -- Delete task

For task APIs, include header:

    Authorization: Bearer <your-jwt-token>

If Swagger is enabled:

    http://localhost:8080/swagger-ui.html

or

    http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------

## Redis Caching

Spring caching is enabled using Redis cache manager.

-   `@Cacheable` on read methods (e.g., get task by ID)\
-   `@CacheEvict` on update/delete methods

Flow:

-   First request → MySQL → Cached in Redis\
-   Subsequent requests → Served from Redis

------------------------------------------------------------------------

## RabbitMQ Notifications

When a new task is created:

-   Service publishes a message to a RabbitMQ exchange/queue.\
-   A consumer (`@RabbitListener`) processes the message.\
-   Simulates sending email/notification.

Purpose:

-   Decouple API logic from slow operations.\
-   Demonstrate message-driven architecture.

------------------------------------------------------------------------

# Optional: AWS EC2 Deployment

-   Created t2.micro/t3.micro instance in ap-south-1 (Mumbai).\
-   Installed Java 17 and MySQL on EC2.\
-   Ran Redis and RabbitMQ via Docker.\
-   Copied JAR to `/opt/taskmanager` and ran:

``` bash
java -jar taskmanager-api-0.0.1-SNAPSHOT.jar
```

-   Opened port 8080 in Security Group.\
-   Accessed API via public IP.

------------------------------------------------------------------------

## Possible Improvements (Future Work)

-   File upload to AWS S3 for task attachments.\
-   Standardized API error format.\
-   Advanced filtering (status, priority, date range).\
-   Rate limiting and stronger security hardening.\
-   React frontend to consume this API.

------------------------------------------------------------------------

## About Me

I'm Tejas Pande, a final-year Computer Engineering student from Pune,
India, focusing on backend development with Java and Spring Boot.
