package pl.rafalpiwnik.scoreboard.model;

import java.util.Objects;

public record Game(String homeTeam, String awayTeam, int homeScore, int awayScore) {

    public Game {
        Objects.requireNonNull(homeTeam, "Home team cannot be null");
        Objects.requireNonNull(awayTeam, "Away team cannot be null");

        // todo further validation
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    @Override
    public String toString() {
        return String.format("%s %d - %s %d", homeTeam, homeScore, awayTeam, awayScore);
    }
}
