package pl.rafalpiwnik.scoreboard.service;

import lombok.extern.slf4j.Slf4j;
import pl.rafalpiwnik.scoreboard.data.GameData;
import pl.rafalpiwnik.scoreboard.exception.ScoreBoardException;
import pl.rafalpiwnik.scoreboard.model.Game;
import pl.rafalpiwnik.scoreboard.repository.GameRepository;

import java.time.Clock;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Slf4j
public class ScoreBoardService implements ScoreBoard {

    private final GameRepository gameRepository;
    private final Clock clock;

    public ScoreBoardService(GameRepository gameRepository, Clock clock) {
        this.gameRepository = gameRepository;
        this.clock = clock;
    }

    @Override
    public UUID startGame(String homeTeam, String awayTeam) {
        validateTeamNames(homeTeam, awayTeam);

        var game = new GameData(randomUUID(), homeTeam, awayTeam, clock.instant());
        gameRepository.save(game);
        log.info("Game with id={} started: {} vs {} @ ts={}", game.getId(), homeTeam, awayTeam, game.getStartTime());
        return game.getId();
    }

    private void validateTeamNames(String homeTeam, String awayTeam) {
        Objects.requireNonNull(homeTeam);
        Objects.requireNonNull(awayTeam);

        if (homeTeam.isBlank()) {
            throw new IllegalArgumentException("Home team name cannot be empty");
        }
        if (awayTeam.isBlank()) {
            throw new IllegalArgumentException("Away team name cannot be empty");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home team name cannot be the same as away team name");
        }
    }

    @Override
    public void finishGame(UUID gameId) {
        Objects.requireNonNull(gameId);

        var deleted = gameRepository.deleteById(gameId);
        if (deleted) {
            log.info("Game with id {} finished", gameId);
        } else {
            throw new ScoreBoardException("Game with id " + gameId + " not found");
        }
    }

    @Override
    public void updateScore(UUID gameId, int homeScore, int awayScore) {
        Objects.requireNonNull(gameId);
        validateScores(homeScore, awayScore);

        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ScoreBoardException("Game with id " + gameId + " not found"));
        game.setScores(homeScore, awayScore);
        gameRepository.save(game);

        log.info("Game with id {} updated: {} vs {} - {}:{}", gameId, game.getHomeTeam(), game.getAwayTeam(), homeScore, awayScore);
    }

    private void validateScores(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
    }

    @Override
    public List<Game> getSummary() {
        Comparator<GameData> byTotalScoreDesc = Comparator.comparingInt(GameData::getTotalScore).reversed();
        Comparator<GameData> byStartTimeDesc = Comparator.comparing(GameData::getStartTime).reversed();
        return gameRepository.findAll().stream()
                .sorted(byTotalScoreDesc.thenComparing(byStartTimeDesc))
                .map(Game::fromEntity)
                .toList();
    }
}
