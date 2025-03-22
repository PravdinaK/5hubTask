# Company and Employee Services

## Структура проекта

- **company_service**: Модуль для управления компаниями.
- **discovery_service**: Eureka Server для регистрации модулей.
- **employee_service**: Модуль для управления сотрудниками.
- **gateway_service**: API Gateway для маршрутизации запросов.


## Технологии

- Spring Boot
- Spring Data JPA
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Feign Client для взаимодействия между модулями
- H2 in-memory база данных для разработки
- Docker для контейнеризации

## Настройка и запуск проекта

### Требования

- Docker
- Docker Compose

### **Сборка и запуск**

Проект включает файл `docker-compose.yml`, который создает контейнеры для всех модулей и базы данных.

Соберите и запустите проект с помощью Docker Compose:
docker-compose up --build

### **Подключение к Swagger**
http://localhost:8081/swagger-ui/index.html
http://localhost:8082/swagger-ui/index.html


### **Данные H2**
Для Company Service:
URL: jdbc:h2:mem:company_db
Username: sa
Password: password

Для Employee Service:
URL: jdbc:h2:mem:employees_db
Username: sa
Password: password

### **Примечания**

Для разработки используется H2 база данных в памяти.
Микросервис employee-service зависит от company-service, и наоборот. Они зарегистрированы в Eureka Server для успешного взаимодействия.
Все запросы проходят через API Gateway, который маршрутизирует их к соответствующим сервисам на основе URL.