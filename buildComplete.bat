call .\gradlew build
copy /Y .\build\libs\tora-game-management-service-0.0.1-SNAPSHOT.jar .\src\main\docker\tora-game-management-service-0.0.1-SNAPSHOT.jar
cd .\src\main\docker
docker compose down
docker compose up