# Используем базовый образ Kotlin для сборки
FROM gradle:7-jdk11 AS build
# Устанавливаем права доступа и копируем исходный код
COPY --chown=gradle:gradle . /home/gradle/src
# Рабочая директория для сборки
WORKDIR /home/gradle/src
# Сборка проекта с использованием Gradle
RUN gradle buildFatJar --no-daemon

# Начинаем с минимального образа JDK для продакшена
FROM eclipse-temurin:17-jre
# Указываем порт, который будет использоваться
EXPOSE 8080
# Рабочая директория для приложения
WORKDIR /app
# Копируем собранный JAR из предыдущего слоя
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar

# Генерируем Keystore для SSL
RUN keytool -genkey -alias sampleAlias -keyalg RSA -keystore /app/keystore.jks \
    -storepass parol1810 -keypass parol1810 -dname "CN=localhost, OU=org, O=org, L=city, ST=state, C=country" -validity 365

# Устанавливаем переменные среды для конфигурации
ENV SSL_KEYSTORE_PATH="/app/keystore.jks"
ENV SSL_KEYSTORE_PASSWORD="parol1810"

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/ktor-docker-sample.jar"]