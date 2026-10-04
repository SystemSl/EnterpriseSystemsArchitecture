## Требования
* **Java:** JDK 23
* **СУБД:** PostgreSQL 18
* **Фреймворк:** Spring Boot 4.1.1 (Spring Web, Spring Data JPA, PostgreSQL Driver, Thymeleaf)
* **Сборщик:** Apache Maven

# Инструкция по развертыванию и настройке проекта

Руководство по настройке базы данных, конфигурированию параметров приложения Spring Boot и его запуску.

---

### Шаг 1. Подготовка базы данных (PostgreSQL)
1. Создайте в PostgreSQL новую базу данных с именем **`server_db`**.
2. Выполните скрипт **`init.sql`** для создания структуры таблиц (сущностей предметной области).

---

### Шаг 2. Настройка параметров подключения
1. Откройте конфигурационный файл **`src/main/resources/application.properties`**.
2. Проверьте корректность учетных данных для подключения к вашей СУБД:
    * `spring.datasource.url=jdbc:postgresql://localhost:5432/server_db`
    * `spring.datasource.username=postgres`
    * `spring.datasource.password=ваш_пароль`
3. При необходимости скорректируйте порт приложения (`server.port=8081`).

---

### Шаг 3. Сборка проекта
1. Выполните команду сборки архива: `mvn clean package`

### Шаг 4. Проверка работы
1. Выполнить команду: `java -jar target/EnterpriseSystemsArchitecture-0.0.1-SNAPSHOT.jar`.
2. Проверьте работоспособность базовых операций (`http://localhost:8081/players` и `http://localhost:8081/guilds`):
    * Добавление новой записи через форму.
    * Редактирование существующей записи.
    * Удаление записи.
