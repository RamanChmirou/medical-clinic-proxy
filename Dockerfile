# Этап сборки
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Копируем pom файлы
COPY pom.xml .
COPY model/pom.xml model/
COPY public/pom.xml public/
COPY domain/pom.xml domain/
COPY adapter/pom.xml adapter/

# Копируем исходный код модулей
COPY model model
COPY public public
COPY domain domain
COPY adapter adapter

# Собираем проект
RUN mvn clean package -DskipTests

# Этап выполнения
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/adapter/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]