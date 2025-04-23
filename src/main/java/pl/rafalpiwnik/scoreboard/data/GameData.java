package pl.rafalpiwnik.scoreboard.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
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
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
    }
}
