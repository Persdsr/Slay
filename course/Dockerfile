FROM maven:3.9.9-amazoncorretto-21 AS build

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17

COPY --from=build target/*.jar app.jar

COPY ../.env ./.env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]