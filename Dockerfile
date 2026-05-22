# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew gradlew
COPY settings.gradle.kts build.gradle.kts ./

RUN chmod +x gradlew

RUN set -e; for i in 1 2 3; do ./gradlew --no-daemon --no-parallel dependencies && break || { if [ "$i" -eq 3 ]; then echo "Dependency resolution failed after 3 attempts"; exit 1; else echo "Dependency resolution failed, retrying in 5 seconds... ($i/3)"; sleep 5; fi; }; done

COPY src src

RUN ./gradlew --no-daemon bootJar
# \
#       -x test \
#       -x detekt \
#       -x ktlintCheck \
#       -x ktlintKotlinScriptCheck \
#       -x ktlintMainSourceSetCheck

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --chown=app:app --from=builder /app/build/libs/eduplan-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
