# API Endpoints Quick Reference

## Player-Game Relationship Endpoints

### 1. Get Games for a Player

```
GET /players/{playerId}/games
```

**Description:** Retrieve all games that a specific player is participating in.

**Path Parameters:**

- `playerId` (UUID): The ID of the player

**Responses:**

- `200 OK`: Returns array of Game objects
- `404 Not Found`: Player doesn't exist

**Example:**

```bash
curl http://localhost:8080/players/{playerId}/games
```

---

### 2. Get Players in a Game

```
GET /games/{gameId}/players
```

**Description:** Retrieve all players participating in a specific game.

**Path Parameters:**

- `gameId` (UUID): The ID of the game

**Responses:**

- `200 OK`: Returns array of Player objects
- `404 Not Found`: Game doesn't exist

**Example:**

```bash
curl http://localhost:8080/games/{gameId}/players
```

---

### 3. Add Player to Game

```
POST /games/{gameId}/players/{playerId}
```

**Description:** Add a player to a game. Idempotent - if player is already in the game, no duplicate is created.

**Path Parameters:**

- `gameId` (UUID): The ID of the game
- `playerId` (UUID): The ID of the player to add

**Responses:**

- `200 OK`: Returns the updated Game object with all players
- `404 Not Found`: Game doesn't exist

**Example:**

```bash
curl -X POST http://localhost:8080/games/{gameId}/players/{playerId}
```

**Response (200 OK):**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "players": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "firstName": "Alice",
      "lastName": "Anderson",
      "games": [
        ...
      ]
    },
    {
      "id": "789e0123-e45b-67d8-a901-234567890abc",
      "firstName": "Bob",
      "lastName": "Brown",
      "games": [
        ...
      ]
    }
  ]
}
```

---

### 4. Remove Player from Game

```
DELETE /games/{gameId}/players/{playerId}
```

**Description:** Remove a player from a game. Safe operation - if player is not in the game, no error is raised.

**Path Parameters:**

- `gameId` (UUID): The ID of the game
- `playerId` (UUID): The ID of the player to remove

**Responses:**

- `200 OK`: Returns the updated Game object with remaining players
- `404 Not Found`: Game doesn't exist

**Example:**

```bash
curl -X DELETE http://localhost:8080/games/{gameId}/players/{playerId}
```

**Response (200 OK):**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "players": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "firstName": "Alice",
      "lastName": "Anderson",
      "games": [
        ...
      ]
    }
  ]
}
```

---

## Common Error Response Format

All error responses follow this structure:

```json
{
  "timestamp": "2025-12-08T10:30:45.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Game with id 550e8400-e29b-41d4-a716-446655440000 not found",
  "path": "/games/550e8400-e29b-41d4-a716-446655440000/players"
}
```

---

## HTTP Status Codes

- **200 OK**: Successful GET, POST (add), or DELETE (remove) operation
- **404 Not Found**: Entity (player or game) not found

---

## Testing with Sample Data

After starting the application, sample data is automatically loaded. You can test with:

```bash
# Get all games to find a game ID
curl http://localhost:8080/games

# Get all players to find a player ID
curl http://localhost:8080/players

# Add a player to a game
curl -X POST http://localhost:8080/games/{gameId}/players/{playerId}

# View updated game
curl http://localhost:8080/games/{gameId}/players

# Remove the player
curl -X DELETE http://localhost:8080/games/{gameId}/players/{playerId}

# Verify removal
curl http://localhost:8080/games/{gameId}/players
```

---

## Notes

- **UUIDs**: All IDs are UUIDs - copy them from GET responses
- **Idempotency**: Adding a player already in the game won't create duplicates
- **Safe Removal**: Removing a player not in the game won't cause errors
- **Response Format**: Add/Remove operations return the full updated Game object

| Code | Meaning               | When Used                         |
|------|-----------------------|-----------------------------------|
| 200  | OK                    | Successful GET request            |
| 201  | Created               | Successful POST request           |
| 204  | No Content            | Successful DELETE request         |
| 400  | Bad Request           | Invalid input (validation failed) |
| 404  | Not Found             | Entity doesn't exist              |
| 500  | Internal Server Error | Unexpected server error           |

---

## Exception Handling

Both endpoints use unified exception handling:

**Exception Hierarchy:**

```
RuntimeException
  └── EntityNotFoundException
        ├── PlayerNotFoundException
        └── GameNotFoundException
```

**Unified Handler:** `RestExceptionHandler.handleEntityNotFoundException()`

**Error Response Format:**

```json
{
  "timestamp": "ISO-8601 datetime",
  "status": 404,
  "error": "Not Found",
  "message": "Description of what wasn't found",
  "path": "/the/request/path"
}
```

---

## Service Layer Methods

### PlayerService

```java
List<Game> getGamesForPlayer(UUID playerId) throws PlayerNotFoundException
```

### GameService

```java
List<Player> getPlayersForGame(UUID gameId) throws GameNotFoundException
```

---

## Test Coverage

### GameRestControllerTest

- testGetPlayersForGame_Success()
- testGetPlayersForGame_GameNotFound()
- testGetPlayersForGame_EmptyGame()

### PlayerRestControllerTest

- testGetGamesForPlayer_Success()
- testGetGamesForPlayer_PlayerNotFound()
- testGetGamesForPlayer_EmptyGames()
- testGetGamesForPlayer_SingleGame()

### GameServiceTest

- testGetPlayersForGame_Success()
- testGetPlayersForGame_GameNotFound()
- testGetPlayersForGame_EmptyGame()
- testGetPlayersForGame_SinglePlayer()

### PlayerServiceTest

- testGetGamesForPlayer_Success()
- testGetGamesForPlayer_PlayerNotFound()
- testGetGamesForPlayer_NoGames()
- testGetGamesForPlayer_SingleGame()

---

## Usage Examples

### Example 1: Get all games for a player

```bash
# Get player ID first (assuming you have one)
PLAYER_ID="550e8400-e29b-41d4-a716-446655440000"

# Get all games for this player
curl -X GET "http://localhost:8080/players/$PLAYER_ID/games" \
  -H "Content-Type: application/json"
```

**Response (if player has 2 games):**

```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440000",
    "players": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "firstName": "John",
        "lastName": "Doe",
        "games": [
          ...
        ]
      },
      {
        "id": "770e8400-e29b-41d4-a716-446655440000",
        "firstName": "Jane",
        "lastName": "Smith",
        "games": [
          ...
        ]
      }
    ]
  },
  {
    "id": "880e8400-e29b-41d4-a716-446655440000",
    "players": [
      ...
    ]
  }
]
```

### Example 2: Get all players in a game

```bash
# Get game ID first (assuming you have one)
GAME_ID="660e8400-e29b-41d4-a716-446655440000"

# Get all players in this game
curl -X GET "http://localhost:8080/games/$GAME_ID/players" \
  -H "Content-Type: application/json"
```

**Response (if game has 2 players):**

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "games": [
      ...
    ]
  },
  {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "firstName": "Jane",
    "lastName": "Smith",
    "games": [
      ...
    ]
  }
]
```

### Example 3: Handle not found error

```bash
# Try to get games for a non-existent player
curl -X GET "http://localhost:8080/players/00000000-0000-0000-0000-000000000000/games"
```

**Response (404 Not Found):**

```json
{
  "timestamp": "2025-12-08T10:30:45.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Player with id 00000000-0000-0000-0000-000000000000 not found",
  "path": "/players/00000000-0000-0000-0000-000000000000/games"
}
```

---

## Integration with Existing Endpoints

These new relationship endpoints complement the existing CRUD endpoints:

**Complete Player Management:**

```
POST   /players                        - Create player
GET    /players/{id}                   - Get player details
GET    /players/{playerId}/games       - Get player's games ✨ NEW
PUT    /players/{id}                   - Update player
DELETE /players/{id}                   - Delete player
GET    /players/search                 - Search by name
```

**Complete Game Management:**

```
POST   /games                          - Create game
GET    /games/{id}                     - Get game details
GET    /games/{gameId}/players         - Get game's players ✨ NEW
PUT    /games/{id}                     - Update game
DELETE /games/{id}                     - Delete game
GET    /games                          - Get all games
```

---

## Notes

- All timestamps are in ISO-8601 format
- All IDs are UUIDs (36 characters with hyphens)
- Lists are never null, but may be empty
- Error messages are automatically sanitized to prevent XSS
- All operations are transactional and consistent
- Read operations use read-only transactions for optimization

