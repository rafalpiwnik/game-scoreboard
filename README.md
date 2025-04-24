# game-scoreboard

## Features

- **Start Game:** Adds a game with score 0-0. Returns a unique `UUID`.
- **Finish Game:** Removes a game using its `UUID`.
- **Update Score:** Updates scores for a game using its `UUID`.
- **Get Summary:** Retrieves ongoing games, ordered by total score (desc), then start time (desc).

## Layout

- `GameRepository` and `ScoreBoard` are high level interfaces that define the contracts and main operations
- `GameData` servers as the data model for the game that is persisted by `GameRepository` while `Game` server as quasi DTO for the public API exposed by `ScoreBoard`
- `InMemoryGameRepository` is the in-memory implementation of `GameRepository` that uses a concurrent hash map to store games
- `ScoreBoardService` is the implementation of `ScoreBoard` that uses `GameRepository` to perform operations

Most holistic test example is `ScoreBoardServiceSpec # should return summary matching the exact example provided in requirements`.

## Assumptions

- Storage: Assumed in-memory storage with concurrent hash map
- Game start validation: I considered prohibiting to start the game when team has an ongoing game, but went with allowing it for simplicity
- Game delete: to remove from scoreboard I assumed hard delete, soft deleting is a consideration but not implemented
- Scores: I assumed that scores are always positive integers and should be validated
- Team names: using some country enum would be more robust, but went with strings without normalization for simplicity
- Printing scoreboard: assumed simple print to stdout using toString for simplicity, but could be improved with printing service to decpuple from toString impl 