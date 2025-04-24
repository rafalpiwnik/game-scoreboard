package pl.rafalpiwnik.scoreboard.service;

import pl.rafalpiwnik.scoreboard.model.Game;

import java.util.List;
import java.util.UUID;

public interface ScoreBoard {

    UUID startGame(String homeTeam, String awayTeam);

    void finishGame(UUID gameId);

    void updateScore(UUID gameId, int homeScore, int awayScore);

    List<Game> getSummary();
}
