package pl.rafalpiwnik.scoreboard.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class GameData {

    private final UUID id;
    private final String homeTeam;
    private final String awayTeam;
    private final Instant startTime;
    private int homeScore;
    private int awayScore;

    public GameData(UUID id, String homeTeam, String awayTeam, Instant startTime) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(homeTeam);
        Objects.requireNonNull(awayTeam);
        Objects.requireNonNull(startTime);

        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
    }

    public void setScores(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }
}
