# Docker для EduPlan

## 📋 Содержание
- Основные команды
- Управление приложением
- Работа с контейнерами
- Работа с образами
- Диагностика и отладка
- Документация Dockerfile
- Документация docker-compose.yml
- Документация .dockerignore

---

## 🚀 Основные команды

### Быстрый старт

```bash
# Запустить приложение в фоне
docker-compose up -d

# Остановить и удалить контейнеры
docker-compose down

# Перезапустить приложение
docker-compose restart

# Посмотреть логи
docker-compose logs -f

# Пересобрать и запустить
docker-compose up -d --build
```

### Полный цикл управления

```bash
# Запуск с пересборкой
docker-compose up -d --build

# Остановка с удалением томов (очистка базы данных)
docker-compose down -v

# Остановка с удалением всех ресурсов
docker-compose down --rmi all -v

# Просмотр статуса
docker-compose ps
```

---

## 🎮 Управление приложением

### Запуск и остановка

| Команда | Описание | Флаги |
|---------|----------|-------|
| `docker-compose up` | Запуск всех сервисов | `-d` - в фоне<br>`--build` - пересобрать перед запуском<br>`--force-recreate` - пересоздать контейнеры |
| `docker-compose down` | Остановка и удаление | `-v` - удалить volumes<br>`--rmi all` - удалить образы<br>`--remove-orphans` - удалить лишние контейнеры |
| `docker-compose start` | Запуск остановленных | `service-name` - конкретный сервис |
| `docker-compose stop` | Остановка без удаления | `-t, --timeout` - таймаут остановки |

### Примеры

```bash
# Запуск конкретного сервиса
docker-compose up -d db

# Остановка с таймаутом 10 секунд
docker-compose stop -t 10

# Полная очистка
docker-compose down -v --remove-orphans
```

---

## 📦 Работа с контейнерами

### Управление контейнерами

```bash
# Вход в контейнер
docker-compose exec app sh
docker-compose exec db psql -U eduplan -d eduplan

# Просмотр логов конкретного сервиса
docker-compose logs -f app
docker-compose logs --tail=100 db

# Копирование файлов
docker cp eduplan-app:/app/app.jar ./backup.jar
docker cp ./backup.jar eduplan-app:/app/app.jar

# Просмотр процессов в контейнере
docker-compose top

# Статистика использования ресурсов
docker stats eduplan-app eduplan-db
```

### Полезные флаги для команд с контейнерами

| Команда | Флаг | Описание |
|---------|------|----------|
| `docker-compose logs` | `-f, --follow` | Следить за логами |
| | `--tail=50` | Показать последние 50 строк |
| | `-t, --timestamps` | Показать временные метки |
| `docker-compose exec` | `-it` | Интерактивный режим с TTY |
| | `-u, --user` | Запуск от конкретного пользователя |
| | `-w, --workdir` | Рабочая директория |

---

## 🖼 Работа с образами

### Управление образами

```bash
# Сборка образа приложения
docker-compose build app
docker-compose build --no-cache app

# Просмотр образов
docker images

# Удаление образов
docker rmi eduplan-app
docker image prune -a

# Сохранение и загрузка образа
docker save eduplan-app > eduplan-app.tar
docker load < eduplan-app.tar
```

### Флаги сборки

| Флаг | Описание |
|------|----------|
| `--no-cache` | Сборка без использования кэша |
| `--pull` | Всегда скачивать свежие базовые образы |
| `--build-arg` | Передача аргументов сборки |
| `--progress=plain` | Подробный вывод процесса сборки |

---

## 🔍 Диагностика и отладка

### Проверка состояния

```bash
# Проверка конфигурации
docker-compose config

# Просмотр сети
docker network ls
docker network inspect eduplan_default

# Информация о контейнере
docker inspect eduplan-app

# Проверка здоровья
docker inspect --format='{{json .State.Health}}' eduplan-db

# Метрики
docker stats --no-stream
```

### Часто используемые комбинации

```bash
# Полный рестарт с очисткой
docker-compose down -v && docker-compose up -d --build

# Просмотр логов с фильтром
docker-compose logs -f | grep ERROR

# Проверка доступности БД
docker-compose exec db pg_isready -U eduplan

# Резервное копирование БД
docker-compose exec db pg_dump -U eduplan eduplan > backup.sql

# Восстановление БД
cat backup.sql | docker-compose exec -T db psql -U eduplan -d eduplan
```

---

## 📝 Документация Dockerfile

```dockerfile
# СБОРКА (Builder Stage)
FROM eclipse-temurin:21-jdk-alpine AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем Gradle wrapper и файлы конфигурации
COPY gradle gradle          # копирует папку с wrapper'ом
COPY gradlew gradlew        # копирует скрипт запуска Gradle
COPY settings.gradle.kts build.gradle.kts ./  # копирует файлы настроек проекта

# Делаем gradlew исполняемым
RUN chmod +x gradlew

# Загружаем зависимости (используем || true для игнорирования ошибок)
RUN ./gradlew --no-daemon --no-parallel dependencies || true

# Копируем исходный код
COPY src src

# Собираем JAR файл, отключая тесты и линтеры для ускорения
RUN ./gradlew --no-daemon bootJar \
      -x test \                    # пропускаем тесты
      -x detekt \                   # пропускаем статический анализ
      -x ktlintCheck \              # пропускаем проверку стиля
      -x ktlintKotlinScriptCheck \  # пропускаем проверку Kotlin скриптов
      -x ktlintMainSourceSetCheck   # пропускаем линтинг основного кода

# Runtime Stage
# Используем JRE (меньше размером) для запуска
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Создаём непривилегированного пользователя для безопасности
RUN addgroup -S app && adduser -S app -G app
USER app

# Копируем собранный JAR из builder stage
COPY --chown=app:app --from=builder /app/build/libs/eduplan-0.0.1-SNAPSHOT.jar app.jar

# Указываем порт приложения
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Что делает каждая строка:

| Строка | Объяснение |
|--------|------------|
| `FROM ... AS builder` | Начинает первый этап сборки, используя JDK 21 на Alpine Linux |
| `WORKDIR /app` | Создаёт и переходит в папку /app |
| `COPY gradle gradle` | Копирует папку gradle (содержит wrapper) |
| `COPY gradlew gradlew` | Копирует скрипт Gradle wrapper |
| `COPY settings.gradle.kts build.gradle.kts ./` | Копирует файлы конфигурации Gradle |
| `RUN chmod +x gradlew` | Дает права на выполнение скрипта |
| `RUN ./gradlew ... \|\| true` | Загружает зависимости, игнорируя ошибки |
| `COPY src src` | Копирует исходный код приложения |
| `RUN ./gradlew bootJar -x test ...` | Собирает JAR без тестов |
| `FROM eclipse-temurin:21-jre-alpine` | Второй этап - используем JRE |
| `RUN addgroup/adduser` | Создаёт системного пользователя |
| `USER app` | Переключается на непривилегированного пользователя |
| `COPY --from=builder ...` | Копирует JAR из первого этапа |
| `EXPOSE 8080` | Указывает порт (документация) |
| `ENTRYPOINT [...]` | Команда запуска приложения |

---

## 📝 Документация docker-compose.yml

```yaml
# Версия формата Docker Compose
version: "3.9"

# Сервисы (контейнеры)
services:
  # База данных PostgreSQL
  db:
    # Используем готовый образ PostgreSQL 17 на Alpine
    image: postgres:17-alpine
    # Имя контейнера
    container_name: eduplan-db
    # Переменные окружения
    environment:
      POSTGRES_DB: eduplan      # Имя базы данных
      POSTGRES_USER: eduplan    # Пользователь БД
      POSTGRES_PASSWORD: eduplan # Пароль пользователя
    # Проброс портов
    ports:
      - "5432:5432"  # порт на хосте:порт в контейнере
    # Постоянное хранилище
    volumes:
      - pgdata:/var/lib/postgresql/data  # сохраняем данные в томе
    # Проверка здоровья
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U eduplan"]  # команда проверки
      interval: 5s   # проверять каждые 5 секунд
      timeout: 5s    # ждать ответа 5 секунд
      retries: 5     # 5 попыток до пометки "unhealthy"

  # Приложение
  app:
    # Сборка из Dockerfile в текущей папке
    build: .
    container_name: eduplan-app
    ports:
      - "8080:8080"  # порт приложения
    # Переменные окружения для приложения
    environment:
      DB_URL: jdbc:postgresql://db:5432/eduplan  # URL подключения к БД
      DB_USERNAME: eduplan
      DB_PASSWORD: eduplan
    # Зависимости
    depends_on:
      db:
        condition: service_healthy  # ждём здоровую БД

# Декларация томов
volumes:
  pgdata:  # том для данных PostgreSQL
```

### Что делает каждая секция:

| Секция/Параметр | Объяснение |
|-----------------|------------|
| `version: "3.9"` | Используется современный формат docker-compose |
| `services:` | Начало описания контейнеров |
| `db:` | Название сервиса базы данных |
| `image: postgres:17-alpine` | Используем официальный образ PostgreSQL на Alpine |
| `container_name:` | Явное имя контейнера для удобства |
| `environment:` | Переменные окружения для конфигурации |
| `ports:` | Проброс портов для доступа с хоста |
| `volumes:` | Монтируем том для сохранения данных |
| `healthcheck:` | Проверка готовности сервиса |
| `depends_on:` | Указывает порядок запуска |
| `condition: service_healthy` | Ждать не просто запуска, а готовности |
| `volumes:` | Декларация томов внизу файла |

---

## 📝 Документация .dockerignore

```
.idea              # Исключаем настройки IntelliJ IDEA
.gradle            # Исключаем кэш Gradle
build              # Исключаем скомпилированные классы
.git               # Исключаем историю Git
.gitignore         # Исключаем конфигурацию Git
HW                 # Исключаем папку с домашними заданиями
*.md               # Исключаем все markdown файлы
docker-compose.yml # Исключаем compose файл
Dockerfile         # Исключаем Dockerfile (копируется отдельно)
```

### Зачем нужен .dockerignore:

| Паттерн | Почему исключаем |
|---------|------------------|
| `.idea/` | Папка IDE не нужна в образе |
| `.gradle/` | Кэш сборки пересоздастся в контейнере |
| `build/` | Скомпилированные классы пересоберутся |
| `.git/` | История версий не нужна в образе |
| `*.md` | Документация не нужна в рантайме |
| `docker-compose.yml` | Не нужен внутри образа |

---
