# 🧩 Архитектура микросервисного приложения Slay

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.2-brightgreen)
![Kafka](https://img.shields.io/badge/Apache_Kafka-3.4.0-ff69b4)
![Redis](https://img.shields.io/badge/Redis-7.0-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.2-blue)

Микросервисное приложение на Spring Boot с использованием Kafka, Redis, PostgreSQL и Docker. Каждый сервис отвечает за свою зону ответственности и взаимодействует с другими по HTTP и Kafka.

## 📦 Стек технологий

| Категория        | Технологии                         |
|------------------|------------------------------------|
| Язык             | Java 17                            |
| Фреймворк        | Spring Boot 3.2                    |
| Безопасность     | Spring Security, JWT               |
| Аутентификация   | Token-based (JWT)                  |
| Брокер сообщений | Apache Kafka                       |
| Базы данных      | PostgreSQL (хранение), Redis (кеш) |
| Тестирование     | JUnit                              |
| Мониторинг       | Spring Actuator                    |
| Деплой           | Docker, Docker Compose             |

## 🧠 Архитектура (Mermaid)

```mermaid
flowchart TD
    A[Клиент] -->|HTTP| B[API Gateway]

    subgraph Backend Services
        B --> C[User Service]
        B --> D[Course Service]
        B --> E[Complaint Service]
        B --> F[Support Service]
        B --> G[Notification Service]
    end

    subgraph Infrastructure
        K[Kafka Broker]
        DB[(PostgreSQL Database)]
        R[(Redis Cache)]
    end

    C -->|DB & Cache| DB
    C --> R
    D -->|DB & Cache| DB
    D --> R
    E -->|DB & Cache| DB
    E --> R
    F -->|DB & Cache| DB
    F --> R
    G -->|DB & Cache| DB
    G --> R

    C <--> K
    D <--> K
    E <--> K
    F <--> K
    G <--> K
```
