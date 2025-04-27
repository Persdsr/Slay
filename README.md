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

    subgraph "Микросервисы"
        B --> C[User Service]
        B --> D[Course Service]
        B --> E[Complaint Service]
        B --> F[Support Service]
        B --> G[Notification Service]
    end

    subgraph "Инфраструктура"
        K[Kafka]
        DB[(PostgreSQL)]
        Redis[(Redis)]
    end

    C & D & E & F & G --> DB
    C & D & E & F & G --> Redis

    C <-.-> K
    D <-.-> K
    E <-.-> K
    F <-.-> K
    G <-.-> K

    classDef service fill:#21c483, stroke:#1F1F23, color: #fff, font-weight: 900,stroke-width:2px, font-size: 12px, font-size: 20px;
    classDef kafka fill:#fff, stroke:#111, color:#111, font-weight: 900,stroke-width:2px, font-size: 22px;
    classDef redis fill:#c6302b, stroke:#fff, font-weight: 900,stroke-width:2px;
    classDef db fill:#2f6792, stroke:#fff, font-weight: 900,stroke-width:2px;
    classDef base fill:#1F1F23, stroke:#fff, font-weight: 900,stroke-width:2px;

    class C,D,E,F,G service
    class Redis redis
    class K kafka
    class DB db
    class A,B base
```
