# Message Service

### Задача

Реализовать сервер, который выполняет следующие задачи:

- Подключение клиентов по websocket. После подключения клиент передаёт уникальный идентификатор на сервер.
- Получать HTTP запрос от издателя. Запрос содержит два поля: id клиента и текст сообщения.
- После получения запроса от издателя сообщение отправляется клиенту.

Сервер должен горизонтально масштабироваться.

### Стэк

- Java 8
- RabbitMQ
- Docker

### Запуск

Для проекта необходим сервер брокера сообщений. Для этого необходимо выполнить команду:

```bash
docker-compose.exe up -d
```

Далее необходимо запустить сервер. Для этого выполнить команду:

```bash
./gradlew bootRun
```

Подключение клиента выполняется по адресу: ws://localhost:8080/connect

Получение сообщения от издателя выполняется по адресу: http://localhost:8080/message/publish

Пример тела сообщения от издателя представлен ниже.

```json
{
    "uuid": "7d5c0aae-24a6-e706-8fdb-5d90a9308289",
    "payload" : "Its new message for users"
}
```
