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
    A["🌐 Клиент (Web/Mobile)"] -->|HTTP/HTTPS| B["🚪 API Gateway"]

    subgraph Backend Services
        B --> C["👤 User Service\n(JWT Auth, Profiles)"]
        B --> D["📚 Course Service\n(Learning Content)"]
        B --> E["⚠️ Complaint Service\n(Moderation)"]
        B --> F["🛟 Support Service\n(Helpdesk)"]
        B --> G["🔔 Notification Service\n(Emails/Push)"]
    end

    subgraph Infrastructure["🛠️ Infrastructure"]
        K["📡 Kafka Broker\n(Event Bus)"]
        DB["🗄️ PostgreSQL\n(Main Data)"]
        R["⚡ Redis\n(Cache/Sessions)"]
    end

%% Database connections
    C & D & E & F & G --> DB
    C & D & E & F & G --> R

%% Event-driven connections
    C -.->|User Events| K
    D -.->|Course Events| K
    E -.->|Complaint Events| K
    F -.->|Support Events| K
    G -.->|Notifications| K

    K -.-> C
    K -.-> D
    K -.-> E
    K -.-> F
    K -.-> G

%% Styling
    classDef client fill:#f9f,stroke:#333,stroke-width:2px;
    classDef gateway fill:#7af,stroke:#333,stroke-width:2px;
    classDef service fill:#9f9,stroke:#333,stroke-width:2px;
    classDef infra fill:#f96,stroke:#333,stroke-width:2px;
    classDef db fill:#69f,stroke:#333,stroke-width:2px;

    class A client
    class B gateway
    class C,D,E,F,G service
    class K infra
    class DB,R db
```
