## Требования
* **Java:** JDK 23
* **СУБД:** PostgreSQL 18
* **Фреймворк:** Spring Boot 4.1.1 (Spring Web, Spring Data JPA, PostgreSQL Driver, Thymeleaf, Jackson Dataformat XML, Spring JMS, ActiveMQ Artemis, Spring Mail)
* **Сборщик:** Apache Maven

---

# Обоснование архитектуры асинхронного обмена сообщениями (JMS)

Для реализации подсистем аудита и почтовых оповещений была интегрирована служба сообщений JMS на базе встроенного брокера **Apache Artemis**.

### 1. Выбор типа объекта назначения: Topic вместо Queue
Ключевым архитектурным решением стал выбор модели **Publish-Subscribe**:
* **Queue (Point-to-Point):** доставляет сообщение только одному из доступных слушателей (потребителей).
* **Topic (Publish-Subscribe):** рассылает копию опубликованного события **всем** активным подписчикам одновременно.

Поскольку в системе действуют два независимых потребителя одного и того же события изменения данных:
1. **Служба журналирования (AuditLogListener):** фиксирует каждое событие в таблицу базы данных `audit_logs`.
2. **Служба уведомлений (NotificationListener):** проверяет событие и формирует email-оповещение администратору.

Использование очереди привело бы к тому, что событие обработал бы либо только логгер, либо только почтовый сервис. Режим топика (`spring.jms.pub-sub-domain=true`) гарантирует параллельную и независимую доставку обоим слушателям.

### 2. Реализация потребителей (MDP — Message-Driven POJO)
В качестве потребителей выступают компоненты со стандартной для Spring аннотацией `@JmsListener`:
* **`AuditLogListener`** — валидирует структуру поступившего `EntityChangeEvent` и сохраняет запись в таблицу аудита.
* **`NotificationListener`** — анализирует тип операции (оповещение настроено на любые модификации данных: `INSERT`, `UPDATE`, `DELETE`) и отправляет сформированное письмо через `JavaMailSender`.

---

# Инструкция по развертыванию и настройке проекта

Руководство по настройке базы данных, конфигурированию параметров приложения Spring Boot и его запуску.

---

### Шаг 1. Подготовка базы данных (PostgreSQL)
1. Создайте в PostgreSQL новую базу данных с именем **`server_db`**.
2. Выполните скрипт **`init.sql`** для создания структуры таблиц сущностей предметной области (`players`, `guilds`) и таблицы аудита (`audit_logs`).

---

### Шаг 2. Настройка параметров подключения
1. Откройте конфигурационный файл **`src/main/resources/application.properties`**.
2. Проверьте корректность учетных данных для подключения к вашей СУБД:
    * `spring.datasource.url=jdbc:postgresql://localhost:5432/server_db`
    * `spring.datasource.username=postgres`
    * `spring.datasource.password=ваш_пароль`
3. При необходимости скорректируйте порт приложения (`server.port=8081`) и адрес получателя уведомлений (`app.mail.recipient=admin@example.com`).

---

### Шаг 3. Сборка проекта
1. Выполните команду сборки архива:
   ```bash
   mvn clean package
   ```

---

### Шаг 4. Запуск и проверка работы

1. Запустите скомпилированное приложение:
   ```bash
   java -Djava.security.manager=allow -jar target/EnterpriseSystemsArchitecture-0.0.1-SNAPSHOT.jar
   ```
   *(Флаг `-Djava.security.manager=allow` необходим для корректного запуска встроенного брокера Artemis в среде Java 23)*.

2. **Проверка асинхронного аудита (JMS) и email-оповещений:**
    * Выполните любое изменение данных (добавление, редактирование или удаление игрока/гильдии) через веб-интерфейс или REST API.
    * Убедитесь, что в консоли приложения зафиксирована параллельная работа обоих слушателей:
      ```text
      [JMS AuditLogListener] Получено событие: [INSERT] Player ...
      [JMS AuditLogListener] Запись успешно добавлена в таблицу audit_logs ...
      [JMS NotificationListener] Получено событие для анализа: [INSERT] Player ...
      [JMS NotificationListener] [Имитация Email на admin@example.com] ...
      ```
    * Проверьте появление записей аудита в PostgreSQL:
      ```sql
      SELECT * FROM audit_logs ORDER BY id DESC;
      ```

3. **Проверка XSLT-отображения в веб-браузере:**
    * `http://localhost:8081/api/players` — список игроков, оформленный через `players.xsl` с ссылками на гильдии.
    * `http://localhost:8081/api/guilds` — список гильдий, оформленный через `guilds.xsl` с ссылками на лидеров.

4. **Проверка REST API (JSON / XML) через консоль или Postman:**
    * Получение данных в формате JSON:
      ```bash
      curl -H "Accept: application/json" http://localhost:8081/api/players
      ```
    * Получение данных в сыром формате XML:
      ```bash
      curl -H "Accept: application/xml" http://localhost:8081/api/players
      ```
    * Создание сущности через JSON:
      ```bash
      curl -X POST http://localhost:8081/api/players \
           -H "Content-Type: application/json" \
           -d "{\"nickname\":\"Tester\",\"level\":10,\"characterClass\":\"Mage\",\"race\":\"Elf\"}"
      ```

5. **Проверка стандартного веб-интерфейса (Thymeleaf):**
    * `http://localhost:8081/players`
    * `http://localhost:8081/guilds`