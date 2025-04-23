package pl.rafalpiwnik.scoreboard.service;

import lombok.RequiredArgsConstructor;
import pl.rafalpiwnik.scoreboard.model.Game;
import pl.rafalpiwnik.scoreboard.repository.GameRepository;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ScoreBoardService implements ScoreBoard {

    private final GameRepository gameRepository;
    private final Clock clock;

    @Override
    public UUID startGame(String homeTeam, String awayTeam) {
        return null;
    }

    @Override
    public void finishGame(UUID gameId) {

    }

    @Override
    public void updateScore(UUID gameId, int homeScore, int awayScore) {

    }

    @Override
    public List<Game> getSummary() {
        return List.of();
    }
}
