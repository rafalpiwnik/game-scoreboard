package pl.rafalpiwnik.scoreboard;

import pl.rafalpiwnik.scoreboard.model.Game;

import java.util.List;
import java.util.UUID;

public interface ScoreBoard {

    // start game with initial score 0-0, return uuid of the game
    UUID startGame(String homeTeam, String awayTeam);

    void finishGame(UUID gameId);

    void updateScore(UUID gameId, int homeScore, int awayScore);

    // ongoing games, ordered by total score desc (highest total first), start time desc (most recent first)
    List<Game> getSummary();

}
