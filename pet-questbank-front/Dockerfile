# Этап сборки. Здесь используется официальный образ Gradle с JDK 21.
FROM gradle:8-jdk-21-and-22-alpine as builder
WORKDIR /home/gradle/project
# Копируем все файлы проекта в образ.
COPY --chown=gradle:gradle . .
# Выполняем сборку проекта.
RUN gradle clean build --no-daemon

# Финальный этап: используем минимальный образ OpenJDK 21 для выполнения приложения.
FROM openjdk:21-jdk-slim
VOLUME /tmp
# Копируем собранный jar-файл из этапа сборки.
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
