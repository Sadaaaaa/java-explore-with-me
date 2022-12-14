# Java app: Explore-with-me
### Дипломный проект Yandex Practicum

Ссылка на pull request:
[Pull Request ExploreWithMe app](https://github.com/Sadaaaaa/java-explore-with-me/pull/1)

API для [Swagger](https://editor-next.swagger.io/) :  
Главный сервер: [ewm-main-service-spec.json](https://github.com/Sadaaaaa/java-explore-with-me/blob/main/ewm-main-service-spec.json)   
Сервер статистики: [ewm-stats-service-spec.json](https://github.com/Sadaaaaa/java-explore-with-me/blob/main/ewm-stats-service-spec.json)

Запуск приложения:

С помощью IntelliJ IDEA
- открываем проект с приложением
- запускаем команду **mvn clean package** в Maven
- запускаем команду **docker-compose up** в терминале IDEA  (Docker daemon должен быть запущен)

Фича: Добавление комментариев к событиям.  

Эндпоинты:  
Добавить коммент: http://localhost:8080/users/{userId}/events/{eventId}/comment  
Изменить коммент: http://localhost:8080/users/{userId}/events/{eventId}/comment/{commentId}   
Удалить коммент: http://localhost:8080/users/{userId}/events/{eventId}/comment/{commentId}   
Получить коммент по его id: http://localhost:8080/comment/{commentId}  
Получить все комменты по id события: http://localhost:8080/comment/all/{eventId}
