# Horoscope Service

Веб-приложение "Гороскоп как сервис" с интеграцией GigaChat API. Позволяет генерировать структурированные гороскопы с настраиваемыми параметрами: тональность, формальность, уровень абсурда.

## Стек технологий

- Java 21
- Spring Boot 3.2.5 (Web, Security, Data JPA, Validation)
- PostgreSQL 18
- Flyway (миграции БД)
- JWT (JSON Web Token)
- BCrypt (хеширование паролей)
- GigaChat API (генерация текста)
- JUnit 5 + Mockito (тестирование)
- H2 (тестовая БД)
- Swagger/OpenAPI (документация API)

## Установка и запуск

### Требования

- JDK 21
- PostgreSQL (локально или в Docker)
- Maven
- Ключ доступа к GigaChat API

### Настройка базы данных

1. Создать базу данных в PostgreSQL:
   CREATE DATABASE horoscope_db;

2. Настроить подключение в src/main/resources/application.yml:
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/horoscope_db
       username: postgres
       password: ваш_пароль

3. Миграции Flyway применятся автоматически при первом запуске.

### Настройка GigaChat API

Добавить ключ доступа в application.yml:
   gigachat:
     client-id: ваш_authorisation_key
     scope: GIGACHAT_API_PERS

### Запуск приложения

   mvn clean package -DskipTests
   mvn spring-boot:run

Приложение будет доступно по адресу: http://localhost:8080

Для удобства запуска подготовлен BAT-скрипт start.bat, который принудительно использует JDK 24 независимо от системных настроек. 


### Запуск тестов

   mvn test

Или через IntelliJ IDEA: правый клик на src/test/java -> Run 'All Tests'.

## API Endpoints

### Аутентификация

- POST /api/auth/register — регистрация нового пользователя
- POST /api/auth/login — вход и получение JWT-токена

### Гороскопы (требуют JWT-токен в заголовке Authorization: Bearer <token>)

- POST /api/horoscope/generate — создание и генерация гороскопа
- GET /api/horoscope/history — история запросов пользователя
- GET /api/horoscope/{id} — получение конкретного гороскопа
- POST /api/horoscope/{id}/save — сохранение результата
- POST /api/horoscope/{id}/regenerate — повторная генерация
- DELETE /api/horoscope/{id} — удаление гороскопа
- POST /api/horoscope/compare — сравнение двух версий
- GET /api/horoscope/limits — проверка оставшихся запросов (лимит 20 в день)

### Документация API

Swagger UI доступен по адресу: http://localhost:8080/swagger-ui/index.html

## Параметры генерации

- characteristic — описание ситуации или знак зодиака
- tone — тональность: sarcastic, motivational, mystical, serious
- formality — формальность: low, medium, high
- absurdityLevel — уровень абсурда: 0-100

## Результат генерации

- generalForecast — общий прогноз
- careerBlock — карьерный блок
- dangerousDays — опасные дни
- whatNotToDo — что не делать

## Паттерны проектирования

- Decorator — динамическое построение промптов (ToneDecorator, AbsurdityDecorator, FormalityDecorator)
- Facade — HoroscopeService скрывает сложность взаимодействия с GigaChat API
- DTO — разделение Entity и API-контрактов
- Singleton — Spring-bean
- Repository — абстракция над слоем доступа к данным
- Chain of Responsibility — SecurityFilterChain для обработки запросов

## Тестирование

19 тестов: 11 unit + 8 интеграционных.

### Unit-тесты

- UserServiceTest — регистрация, аутентификация, отклонение неверного пароля
- PromptDecoratorTest — декораторы абсурда, тона, формальности и их цепочка
- GigaChatPromptBuilderTest — парсинг JSON, очистка маркдауна, обработка ошибок

### Интеграционные тесты

- UserRepositoryTest — сохранение, поиск по email, проверка существования
- AuthControllerTest — регистрация, вход, 401/403, доступ с токеном

## Скриншоты

### Главная страница
<img width="980" height="431" alt="image" src="https://github.com/user-attachments/assets/457e2d18-d375-4295-9cc8-e0ee3f8053b9" />


### Сгенерированный гороскоп
<img width="980" height="454" alt="image" src="https://github.com/user-attachments/assets/97e148ee-8228-4d90-b2a5-92243d4ba0f6" />


### Сравнение версий
<img width="980" height="434" alt="image" src="https://github.com/user-attachments/assets/8f829d85-297b-4a5c-8f67-f6ba553522f8" />
<img width="980" height="458" alt="image" src="https://github.com/user-attachments/assets/63104208-ad09-4129-ae78-fcf35e17b203" />


## Автор

Студент Мухин Михаил, Б23-901, 2026
