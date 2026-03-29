# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY settings.gradle.kts build.gradle.kts ./

RUN chmod +x gradlew

RUN ./gradlew --no-daemon --no-parallel dependencies

COPY src src

RUN ./gradlew --no-daemon bootJar

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --chown=app:app --from=builder /app/build/libs/eduplan-0.0.1-SNAPSHOT.jar app.jar

COPY --chown=app:app .env ./


USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
