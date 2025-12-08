# Tora Game Management Service

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A RESTful web service for managing games and players in a citizen science project. This service provides a complete API
for creating, updating, retrieving, and deleting games and players, along with managing player membership in games.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Sample Data](#sample-data)
- [Testing](#testing)
- [Configuration](#configuration)
- [Additional Documentation](#additional-documentation)
- [License](#license)

## 🎯 Overview

The Tora Game Management Service is a Spring Boot application designed to manage players and games for a citizen science
project. It provides a comprehensive REST API with full CRUD operations and relationship management between players and
games.

### Key Capabilities

- ✅ **Player Management**: Create, read, update, and delete players
- ✅ **Game Management**: Create, read, update, and delete games
- ✅ **Relationship Management**: Add and remove players from games
- ✅ **Search Functionality**: Find players by first name, last name, or both
- ✅ **Automatic Data Initialization**: Sample data loaded on startup for testing
- ✅ **OpenAPI/Swagger Documentation**: Interactive API documentation
- ✅ **H2 Database**: Embedded database for easy development and testing

## ✨ Features

### Player Operations

- Create new players with first and last names
- Update existing player information
- Retrieve player details by ID
- Search players by name
- Delete players
- View all games a player participates in

### Game Operations

- Create new games with multiple players
- Update game player roster
- Retrieve game details by ID
- View all games
- Delete games
- View all players in a game

### Player-Game Management

- **Add Player to Game**: `POST /games/{gameId}/players/{playerId}`
- **Remove Player from Game**: `DELETE /games/{gameId}/players/{playerId}`

## 🛠 Technology Stack

- **Java**: 25
- **Spring Boot**: 4.0.0
- **Spring Data JPA**: For data persistence
- **Spring Web MVC**: For RESTful web services
- **Spring Validation**: For input validation
- **H2 Database**: Embedded database
- **SpringDoc OpenAPI**: For API documentation (Swagger UI)
- **Gradle**: Build automation
- **JUnit 5**: Unit testing
- **Spring REST Docs**: API documentation generation
- **JaCoCo**: Code coverage reporting

## 📁 Project Structure

The application follows a clean layered architecture with clear separation of concerns:

```
src/main/java/edu/kit/datamanager/hector25/tora_game_management_service/
│
├── ToraGameManagementServiceApplication.java    # Main application class
│
├── config/                                      # Configuration classes
│   └── DataInitializer.java                    # Sample data initialization
│
├── domain/                                      # Entity/Domain models
│   ├── Game.java                                # Game entity
│   └── Player.java                              # Player entity
│
├── dao/                                         # Data Access Objects (Repositories)
│   ├── IGameDao.java                            # Game repository interface
│   └── IPlayerDao.java                          # Player repository interface
│
├── service/                                     # Service layer (Business logic)
│   ├── IGameService.java                        # Game service interface
│   ├── IPlayerService.java                      # Player service interface
│   ├── dto/                                     # Data Transfer Objects
│   │   └── PlayerCreationDTO.java              # DTO for player creation
│   └── impl/                                    # Service implementations
│       ├── GameService.java                     # Game service implementation
│       └── PlayerService.java                   # Player service implementation
│
├── web/                                         # Web/REST layer
│   ├── IGameAPI.java                            # Game API interface
│   ├── IPlayerAPI.java                          # Player API interface
│   ├── impl/                                    # REST controllers
│   │   ├── GameRestController.java             # Game REST controller
│   │   └── PlayerRestController.java           # Player REST controller
│   └── exceptionhandling/                      # Exception handling
│       └── RestExceptionHandler.java           # Global exception handler
│
└── exceptions/                                  # Custom exceptions
    ├── GameNotFoundException.java
    └── PlayerNotFoundException.java

src/test/java/                                   # Test sources (mirrors main structure)
src/main/resources/                              # Application resources
    ├── application.properties                   # Application configuration
    └── static/                                  # Static resources
```

### Architecture Overview

The application follows a **layered architecture**:

1. **Web Layer** (`web/`): Handles HTTP requests and responses
    - REST controllers implement API interfaces
    - Exception handlers for error responses
    - Request/response validation

2. **Service Layer** (`service/`): Contains business logic
    - Service interfaces define contracts
    - Service implementations handle business rules
    - Transaction management

3. **DAO Layer** (`dao/`): Data access and persistence
    - Repository interfaces extend Spring Data JPA
    - Database operations abstraction

4. **Domain Layer** (`domain/`): Entity models
    - JPA entities with relationships
    - Domain model validation

5. **Configuration Layer** (`config/`): Application configuration
    - Data initialization
    - Bean definitions

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK)**: Version 25 or later
- **Gradle**: Included via Gradle Wrapper (no separate installation needed)
- **Git**: For cloning the repository

### Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd tora-game-management-service
   ```

2. **Verify Java installation**:
   ```bash
   java -version
   ```
   You should see Java 25 or later.

### Running the Application

#### Using Gradle Wrapper (Recommended)

**On Unix/Linux/macOS**:

```bash
./gradlew bootRun
```

**On Windows**:

```bash
gradlew.bat bootRun
```

#### Using Gradle Build

1. **Build the application**:
   ```bash
   ./gradlew build
   ```

2. **Run the JAR file**:
   ```bash
   java -jar build/libs/tora-game-management-service-0.0.1-SNAPSHOT.jar
   ```

#### Application Startup

Once started, you'll see:

```
Tora Game Management Service started. Running on http://localhost:8080
Swagger UI available at http://localhost:8080/swagger-ui.html
```

The application will automatically initialize with sample data (6 players and 5 games). Check the console logs for
details.

## 📚 API Documentation

### Swagger UI (Interactive Documentation)

The application provides interactive API documentation via Swagger UI:

**URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Features:

- 📖 Complete API documentation
- 🧪 Try out endpoints directly in the browser
- 📝 View request/response schemas
- ✅ See example values
- 🔍 Search and filter endpoints

### OpenAPI Specification

Raw OpenAPI specification available at:

- **JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **YAML**: [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)

### Main API Endpoints

#### Player Endpoints

- `GET /players` - Get all players
- `GET /players/{id}` - Get player by ID
- `POST /players` - Create a new player
- `PUT /players/{id}` - Update a player
- `DELETE /players/{id}` - Delete a player
- `GET /players/{id}/games` - Get games for a player
- `GET /players?firstName={name}` - Search by first name
- `GET /players?lastName={name}` - Search by last name

#### Game Endpoints

- `GET /games` - Get all games
- `GET /games/{id}` - Get game by ID
- `POST /games` - Create a new game
- `PUT /games/{id}` - Update a game
- `DELETE /games/{id}` - Delete a game
- `GET /games/{id}/players` - Get players in a game
- `POST /games/{gameId}/players/{playerId}` - Add player to game
- `DELETE /games/{gameId}/players/{playerId}` - Remove player from game

### Example API Calls

```bash
# Get all players
curl http://localhost:8080/players

# Get all games
curl http://localhost:8080/games

# Add a player to a game
curl -X POST http://localhost:8080/games/{gameId}/players/{playerId}

# Remove a player from a game
curl -X DELETE http://localhost:8080/games/{gameId}/players/{playerId}
```

## 🎲 Sample Data

The application automatically loads sample data on startup for development and testing purposes.

### Default Players

- Alice Anderson
- Bob Brown
- Charlie Clark
- Diana Davis
- Eve Evans
- Frank Foster

### Default Games

- Game 1: Alice, Bob, Charlie (3 players)
- Game 2: Charlie, Diana, Eve, Frank (4 players)
- Game 3: Alice, Diana (2 players)
- Game 4: Bob, Eve (2 players)
- Game 5: Alice, Charlie, Eve (3 players)

**See**: [QUICK_START_GUIDE.md](QUICK_START_GUIDE.md) for detailed information on using the sample data.

**See**: [DATA_INITIALIZATION.md](DATA_INITIALIZATION.md) for configuration and customization options.

## 🧪 Testing

### Run All Tests

```bash
./gradlew test
```

### Run Tests with Coverage Report

```bash
./gradlew test jacocoTestReport
```

Coverage reports are generated in: `build/reports/jacoco/test/html/index.html`

### Test Structure

Tests are organized in `src/test/java/` and mirror the main source structure:

- **Unit Tests**: For service layer logic
- **Integration Tests**: For REST controllers
- **Repository Tests**: For data access layer

## ⚙️ Configuration

### Application Properties

Configuration file: `src/main/resources/application.properties`

Key configurations:

```properties
# Server Configuration
server.port=8080

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:file:/tmp/database;MODE=LEGACY;NON_KEYWORDS=VALUE
spring.datasource.username=username
spring.datasource.password=secure_me
spring.jpa.hibernate.ddl-auto=update

# Logging
logging.level.root=WARN
logging.level.org.springframework=WARN
logging.level.edu.kit.datamanager=DEBUG

# Profiles
spring.profiles.active=default
```

### Profiles

- **default**: Development profile with data initialization
- **dev**: Development profile with data initialization
- **prod**: Production profile (data initialization disabled)

To change profile:

```properties
spring.profiles.active=prod
```

### Database

The application uses **H2** (embedded database) by default:

- **Type**: File-based
- **Location**: `/tmp/database`
- **Console**: Can be enabled in `application.properties`

**Warning**: The database is stored in `/tmp/` which may be cleared on system restart. For production, configure a
persistent location.

## 📖 Additional Documentation

This project includes comprehensive documentation:

- **[QUICK_START_GUIDE.md](QUICK_START_GUIDE.md)** - Get started quickly with examples and use cases
- **[DATA_INITIALIZATION.md](DATA_INITIALIZATION.md)** - Details on automatic data initialization
- **[IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md)** - Implementation details and architecture decisions
- **[NEW_ENDPOINTS_QUICK_REFERENCE.md](NEW_ENDPOINTS_QUICK_REFERENCE.md)** - Quick reference for API endpoints
- **[HELP.md](HELP.md)** - Spring Boot and framework documentation links

## 🏗️ Building for Production

### Create Production JAR

```bash
./gradlew clean build
```

The executable JAR will be in: `build/libs/tora-game-management-service-0.0.1-SNAPSHOT.jar`

### Run Production JAR

```bash
java -jar build/libs/tora-game-management-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 🔧 Development

### Hot Reload

The project includes Spring Boot DevTools for automatic restart during development.

### Code Quality

- **JaCoCo**: Code coverage reporting
- **Spring Validation**: Input validation
- **JSpecify**: Null-safety annotations

### IDE Support

The project works with:

- IntelliJ IDEA
- Eclipse
- VS Code (with Java extensions)

## 🐛 Troubleshooting

### Port Already in Use

If port 8080 is already in use:

```properties
server.port=8081
```

### Database Issues

If you encounter database errors:

1. Delete the database file: `rm /tmp/database*`
2. Restart the application

### No Sample Data

If sample data doesn't appear:

1. Check that profile is `default` or `dev`
2. Check application logs for initialization messages
3. Verify `DataInitializer.java` is in the classpath

## 📄 License

Copyright (c) 2025 Karlsruhe Institute of Technology.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## 🤝 Contributing

This is a citizen science project. Contributions are welcome!

## 📧 Contact

Karlsruhe Institute of Technology

Scientific Computing Center

Data Exploitation Methods

Maximilian Inckmann <maximilian.inckmann@kit.edu>

---

**Quick Links**:

- 🌐 API Documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- 📊 Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator)
- 🔍 OpenAPI Spec: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

**Happy Coding! 🚀**

