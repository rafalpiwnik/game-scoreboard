package pl.rafalpiwnik.scoreboard.app;

import pl.rafalpiwnik.scoreboard.model.Game;
import pl.rafalpiwnik.scoreboard.repository.GameRepository;
import pl.rafalpiwnik.scoreboard.repository.InMemoryGameRepository;
import pl.rafalpiwnik.scoreboard.service.ScoreBoard;
import pl.rafalpiwnik.scoreboard.service.ScoreBoardService;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

public class DemoApp {

    public static void main(String[] args) {
        Clock clock = Clock.systemUTC();
        GameRepository repository = new InMemoryGameRepository();
        ScoreBoard board = new ScoreBoardService(repository, clock);

        // Start & Update
        UUID game1 = board.startGame("Mexico", "Canada");
        UUID game2 = board.startGame("Spain", "Brazil");
        board.updateScore(game1, 0, 5);
        board.updateScore(game2, 10, 2);

        // Get Summary
        System.out.println("--- Summary ---");
        List<Game> summary = board.getSummary();
        summary.forEach(System.out::println);

        // Finish Game
        board.finishGame(game1);
        System.out.println("\n--- Summary After Finish ---");
        summary = board.getSummary();
        summary.forEach(System.out::println);
    }
}
