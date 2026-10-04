## Требования
* **Java:** JDK 23
* **СУБД:** PostgreSQL 18
* **Фреймворк:** Spring Boot 4.1.1 (Spring Web, Spring Data JPA, PostgreSQL Driver, Thymeleaf, Jackson Dataformat XML)
* **Сборщик:** Apache Maven

---

# Обоснование выбора технологии: Spring REST против JAX-RS

В проекте для реализации интерфейса веб-сервиса был выбран **Spring REST**.

### Сравнительный анализ:

| Критерий | JAX-RS (Jakarta RESTful Web Services) | Spring REST (Spring Web MVC) |
|---|---|---|
| **Архитектурный базис** | Спецификация Jakarta EE (реализации: Jersey, RESTEasy, Apache CXF). | Часть экосистемы Spring Framework / Spring Boot. |
| **Аннотации** | `@Path`, `@GET`, `@POST`, `@Produces`, `@Consumes`. | `@RestController`, `@GetMapping`, `@PostMapping`, `@RequestBody`. |
| **Среда выполнения** | Требует отдельный сервер приложений (GlassFish, WildFly) либо сложную интеграцию встроенных контейнеров. | Запускается автономно на встроенном сервере (Apache Tomcat) без внешних зависимостей. |
| **Интеграция** | Связывается с EJB/CDI через контейнер Jakarta EE; для связи со сторонними библиотеками требуются адаптеры. | Нативно интегрирован со Spring Data JPA, транзакционным менеджером и Spring Security. |
| **Сериализация данных** | Традиционно опирается на связку JAXB (XML) и JSON-P/JSON-B (или Jackson). | Использует единый конвейер Jackson (через модуль `jackson-dataformat-xml`) для обработки JSON и XML. |

### Причины выбора Spring REST:
1. Проект уже использует Spring Data JPA и IoC-контейнер Spring. Использование Spring REST позволяет обращаться к существующим сервисам (`PlayerService`, `GuildService`) напрямую, без промежуточных мостов между спецификациями.
2. Поддержка форматов JSON и XML включается декларативно на уровне Spring MVC и автоматически обрабатывает стандартные HTTP-заголовки `Accept` и `Content-Type`.
3. Отсутствует необходимость конфигурировать пулы соединений и ресурсы в консолях внешних серверов приложений — всё конфигурируется централизованно через `application.properties`.

---

# Спецификация REST API

Сервис предоставляет доступ к сущностям и поддерживает форматы **JSON** (`application/json`) и **XML** (`application/xml`) как для входящих данных, так и для ответов.

### Игроки (`/api/players`)
* `GET /api/players` — получить список всех игроков.
* `GET /api/players/{id}` — получить игрока по идентификатору.
* `POST /api/players?guildId={id}` — добавить игрока (с опциональной привязкой к гильдии).
* `PUT /api/players/{id}?guildId={id}` — обновить данные игрока.
* `DELETE /api/players/{id}` — удалить игрока.

### Гильдии (`/api/guilds`)
* `GET /api/guilds` — получить список всех гильдий.
* `GET /api/guilds/{id}` — получить гильдию по идентификатору.
* `POST /api/guilds?leaderId={id}` — создать гильдию с назначением лидера.
* `PUT /api/guilds/{id}?leaderId={id}` — изменить данные гильдии.
* `DELETE /api/guilds/{id}` — распустить гильдию (с отвязкой участников).

---

# XSL-преобразование XML-откликов

Для визуализации XML-ответов непосредственно в браузере реализована клиентская XSLT-трансформация:
1. Таблицы стилей размещены в статической части веб-слоя:
   * **`src/main/resources/static/xsl/players.xsl`** — формирует HTML-таблицу игроков с кликабельными ссылками на связанные гильдии.
   * **`src/main/resources/static/xsl/guilds.xsl`** — формирует HTML-таблицу гильдий с навигацией к профилям их лидеров.
2. Через сервлетный фильтр (`XslResponseFilter`) в начало каждого XML-ответа автоматически внедряется инструкция обработки:
   ```xml
   <?xml-stylesheet type="text/xsl" href="/xsl/{сущность}.xsl"?>

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
2. Проверка XSLT-отображения в веб-браузере:
   * `http://localhost:8081/api/players` — список игроков, оформленный через `players.xsl` с ссылками на гильдии.
   * `http://localhost:8081/api/guilds` — список гильдий, оформленный через `guilds.xsl` с ссылками на лидеров.
     *(При просмотре исходного кода страниц `Ctrl + U` отображается исходный XML с тегом `<?xml-stylesheet ...?>`)*.
3. Проверка REST API (JSON / XML) через консоль или Postman:
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
4. **Проверка стандартного веб-интерфейса (Thymeleaf):**
   * `http://localhost:8081/players`
   * `http://localhost:8081/guilds`
