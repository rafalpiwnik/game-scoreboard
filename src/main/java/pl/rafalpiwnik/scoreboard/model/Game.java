package pl.rafalpiwnik.scoreboard.model;

import pl.rafalpiwnik.scoreboard.data.GameData;

import java.util.Objects;

public record Game(String homeTeam, String awayTeam, int homeScore, int awayScore) {

    public Game {
        Objects.requireNonNull(homeTeam, "Home team cannot be null");
        Objects.requireNonNull(awayTeam, "Away team cannot be null");

        // todo further validation
    }

    public static Game fromEntity(GameData gameData) {
        return new Game(gameData.getHomeTeam(), gameData.getAwayTeam(), gameData.getHomeScore(), gameData.getAwayScore());
    }

    @Override
    public String toString() {
        return String.format("%s %d - %s %d", homeTeam, homeScore, awayTeam, awayScore);
    }
}
