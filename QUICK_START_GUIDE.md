# Quick Start Guide - Using Sample Data

## 🚀 Getting Started

After starting the application, you'll have **6 players** and **5 games** already created in the database.

## 📋 Sample Players

| Name           | Description                     |
|----------------|---------------------------------|
| Alice Anderson | Player 1 - In Games 1, 3, and 5 |
| Bob Brown      | Player 2 - In Games 1 and 4     |
| Charlie Clark  | Player 3 - In Games 1, 2, and 5 |
| Diana Davis    | Player 4 - In Games 2 and 3     |
| Eve Evans      | Player 5 - In Games 2, 4, and 5 |
| Frank Foster   | Player 6 - In Game 2 only       |

## 🎮 Sample Games

| Game   | Players                    | Count |
|--------|----------------------------|-------|
| Game 1 | Alice, Bob, Charlie        | 3     |
| Game 2 | Charlie, Diana, Eve, Frank | 4     |
| Game 3 | Alice, Diana               | 2     |
| Game 4 | Bob, Eve                   | 2     |
| Game 5 | Alice, Charlie, Eve        | 3     |

## 💡 Quick Test Commands

### 1. View All Players

```bash
curl http://localhost:8080/players
```

### 2. View All Games

```bash
curl http://localhost:8080/games
```

### 3. Get Players in a Game

First, get all games to find a game ID, then:

```bash
curl http://localhost:8080/games/{gameId}/players
```

### 4. Add a Player to a Game

```bash
curl -X POST http://localhost:8080/games/{gameId}/players/{playerId}
```

### 5. Remove a Player from a Game

```bash
curl -X DELETE http://localhost:8080/games/{gameId}/players/{playerId}
```

## 🌐 Using Swagger UI

1. Open your browser to: `http://localhost:8080/swagger-ui.html`
2. Expand any endpoint
3. Click "Try it out"
4. Execute the request
5. View the response

### Example: Testing with Swagger

1. **Get all players** - Use `GET /players` to see all 6 players
2. **Copy a player ID** - From the response
3. **Get all games** - Use `GET /games` to see all 5 games
4. **Copy a game ID** - From the response
5. **Test add player** - Use `POST /games/{gameId}/players/{playerId}`

## 📊 Understanding the Data Structure

### Player Response Example

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "firstName": "Alice",
  "lastName": "Anderson",
  "games": [
    ...
  ]
}
```

### Game Response Example

```json
{
  "id": "987e6543-e21b-43d2-a321-426614174001",
  "players": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "firstName": "Alice",
      "lastName": "Anderson",
      "games": [
        ...
      ]
    },
    ...
  ]
}
```

## 🔄 Testing Scenarios

### Scenario 1: View a Player's Games

1. Get all players
2. Pick a player (e.g., Alice Anderson)
3. Note which games they're in (Games 1, 3, and 5)
4. Use `GET /players/{playerId}/games` to verify

### Scenario 2: Modify Game Membership

1. Get Game 3 (Alice and Diana)
2. Add Bob to Game 3: `POST /games/{game3Id}/players/{bobId}`
3. Verify: `GET /games/{game3Id}/players` (should now show 3 players)
4. Remove Bob: `DELETE /games/{game3Id}/players/{bobId}`
5. Verify: `GET /games/{game3Id}/players` (back to 2 players)

### Scenario 3: Create a New Game

1. Get player IDs for Alice and Frank
2. Create a new game: `POST /games` with `["aliceId", "frankId"]`
3. Verify with `GET /games`

## ⚠️ Important Notes

- **UUIDs Change**: Player and game IDs will be different each time you restart the app
- **Always fetch IDs first**: Don't hardcode UUIDs in your tests
- **Database Persistence**: Data is stored in `/tmp/database` (configure in application.properties)
- **Reset Data**: Restart the application to reset to initial data

## 🎯 Common Use Cases

### Finding a specific player

```bash
# By first name
curl "http://localhost:8080/players?firstName=Alice"

# By last name
curl "http://localhost:8080/players?lastName=Anderson"

# By both
curl "http://localhost:8080/players?firstName=Alice&lastName=Anderson"
```

### Working with Games

```bash
# Create a game with 3 players
curl -X POST http://localhost:8080/games \
  -H "Content-Type: application/json" \
  -d '["playerId1", "playerId2", "playerId3"]'

# Update a game's players
curl -X PUT http://localhost:8080/games/{gameId} \
  -H "Content-Type: application/json" \
  -d '["playerId1", "playerId4"]'
```

## 🐛 Troubleshooting

### No data appears?

- Check logs for "Starting data initialization..."
- Verify profile is `default` or `dev` in application.properties
- Check for errors during startup

### Can't find IDs?

- Use `GET /players` or `GET /games` first
- Copy IDs from the response
- Don't manually type UUIDs

### Data disappeared?

- If using in-memory database, data resets on restart
- Check `spring.datasource.url` in application.properties
- Current: `jdbc:h2:file:/tmp/database`

## 📚 Next Steps

1. **Explore the API** - Try all endpoints in Swagger UI
2. **Test edge cases** - Add/remove players from games
3. **Create new data** - Add your own players and games
4. **Check persistence** - Restart and see if data persists
5. **Customize** - Modify DataInitializer.java to add your own sample data

Happy coding! 🎉

