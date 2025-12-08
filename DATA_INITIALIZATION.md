# Data Initialization

## Overview

The application includes automatic data initialization that populates the database with sample data when the application
starts. This is useful for development and testing purposes.

## Configuration

The data initializer is configured in:

- **File**: `src/main/java/edu/kit/datamanager/hector25/tora_game_management_service/config/DataInitializer.java`
- **Profile**: Active when `dev` or `default` profile is set

## Sample Data Created

### Players (6 total)

1. **Alice Anderson**
2. **Bob Brown**
3. **Charlie Clark**
4. **Diana Davis**
5. **Eve Evans**
6. **Frank Foster**

### Games (5 total)

1. **Game 1**: Alice, Bob, Charlie (3 players)
2. **Game 2**: Charlie, Diana, Eve, Frank (4 players)
3. **Game 3**: Alice, Diana (2 players)
4. **Game 4**: Bob, Eve (2 players)
5. **Game 5**: Alice, Charlie, Eve (3 players)

## How It Works

The data initializer:

1. Runs automatically at application startup
2. Creates 6 sample players with first and last names
3. Creates 5 sample games with various player combinations
4. Logs each creation operation for visibility
5. Provides a summary of created entities

## Enabling/Disabling

### To Enable (Default)

The initializer runs by default with the `default` or `dev` profile:

```properties
spring.profiles.active=default
```

### To Disable

To disable the data initializer, use a different profile:

```properties
spring.profiles.active=prod
```

Or you can remove/comment out the `@Profile` annotation in the `DataInitializer.java` class.

## Viewing Initialized Data

Once the application starts, you can verify the data through:

1. **Swagger UI**: http://localhost:8080/swagger-ui.html
    - GET `/players` - View all players
    - GET `/games` - View all games
    - GET `/games/{gameId}/players` - View players in a specific game

2. **Direct API Calls**:
   ```bash
   # Get all players
   curl http://localhost:8080/players
   
   # Get all games
   curl http://localhost:8080/games
   ```

3. **H2 Console** (if enabled):
    - Check your `application.properties` for H2 console configuration
    - Default database location: `/tmp/database`

## Customization

To customize the sample data:

1. Open `DataInitializer.java`
2. Modify the player creation section:
   ```java
   Player player1 = playerService.createPlayer(new PlayerCreationDTO("FirstName", "LastName"));
   ```
3. Modify the game creation section:
   ```java
   Game game1 = gameService.createGame(List.of(playerId1, playerId2, ...));
   ```

## Logs

When the application starts, you'll see initialization logs like:

```
INFO  - Starting data initialization...
INFO  - Created player: Alice Anderson (ID: <uuid>)
INFO  - Created player: Bob Brown (ID: <uuid>)
...
INFO  - Created game 1 (ID: <uuid>) with 3 players: Alice, Bob, Charlie
...
INFO  - Data initialization completed successfully!
INFO  - Summary: Created 6 players and 5 games
```

## Notes

- The data initializer uses the existing service layer methods
- All operations are properly logged for debugging
- Player and game IDs are generated automatically by the database
- The initializer is idempotent when the database is empty
- If you restart the application with `spring.jpa.hibernate.ddl-auto=create` or `create-drop`, the data will be
  recreated

