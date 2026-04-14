# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew gradlew
COPY settings.gradle.kts build.gradle.kts ./

RUN chmod +x gradlew
RUN ./gradlew --no-daemon --no-parallel dependencies || true

COPY src src

RUN ./gradlew --no-daemon bootJar \
      -x test \
      -x detekt \
      -x ktlintCheck \
      -x ktlintKotlinScriptCheck \
      -x ktlintMainSourceSetCheck

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --chown=app:app --from=builder /app/build/libs/eduplan-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
