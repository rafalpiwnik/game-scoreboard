package pl.rafalpiwnik.scoreboard.service

import pl.rafalpiwnik.scoreboard.exception.ScoreBoardException
import pl.rafalpiwnik.scoreboard.model.Game
import pl.rafalpiwnik.scoreboard.repository.GameRepository
import pl.rafalpiwnik.scoreboard.repository.InMemoryGameRepository
import spock.lang.Specification
import spock.lang.Subject

import java.time.Clock
import java.time.Instant

import static java.time.Instant.now

class ScoreBoardServiceSpec extends Specification {

    Clock clock = Mock(Clock)
    GameRepository gameRepository = new InMemoryGameRepository()

    @Subject
    ScoreBoard scoreboard = new ScoreBoardService(gameRepository, clock)

    private final static Instant TEST_INSTANT = now()

    def "should start game"() {
        given:
        clock.instant() >> TEST_INSTANT
        def homeTeam = "Mexico"
        def awayTeam = "Canada"

        when:
        def gameId = scoreboard.startGame(homeTeam, awayTeam)

        then:
        def retrievedGame = gameRepository.findById(gameId)
        retrievedGame.isPresent()
        verifyAll(retrievedGame.get()) {
            id == gameId
            homeTeam == homeTeam
            awayTeam == awayTeam
            homeScore == 0
            awayScore == 0
            startTime == TEST_INSTANT
        }
    }

    def "should update score for a game"() {
        given:
        clock.instant() >> TEST_INSTANT
        def gameId = scoreboard.startGame("Mexico", "Canada")

        expect:
        verifyAll(gameRepository.findById(gameId).get()) {
            homeScore == 0
            awayScore == 0
        }

        when:
        scoreboard.updateScore(gameId, 2, 3)

        then:
        def retrievedGame = gameRepository.findById(gameId)
        retrievedGame.isPresent()
        verifyAll(retrievedGame.get()) {
            homeScore == 2
            awayScore == 3
        }
    }

    def "should throw exception when attempting to update game with illegal score"() {
        given:
        clock.instant() >> TEST_INSTANT
        def gameId = scoreboard.startGame("Mexico", "Canada")

        when:
        scoreboard.updateScore(gameId, homeScore, awayScore)

        then:
        def ex = thrown(IllegalArgumentException)

        where:
        homeScore | awayScore
        -1        | 3
        2         | -3
        -5        | -1
    }

    def "should throw exception when updating score for nonexistent game"() {
        given:
        def gameId = UUID.randomUUID()

        when:
        scoreboard.updateScore(gameId, 2, 3)

        then:
        def ex = thrown(ScoreBoardException)
    }

    def "should finish game"() {
        given:
        clock.instant() >> TEST_INSTANT
        def gameId = scoreboard.startGame("Mexico", "Canada")

        when:
        scoreboard.finishGame(gameId)

        then:
        def retrievedGame = gameRepository.findById(gameId)
        retrievedGame.isEmpty()
    }

    def "should throw exception when attempting to finish nonexistent game"() {
        given:
        def gameId = UUID.randomUUID()

        when:
        scoreboard.finishGame(gameId)

        then:
        def ex = thrown(ScoreBoardException)
    }

    def "should return summary list ordered by total score"() {
        given:
        clock.instant() >> TEST_INSTANT
        def game1 = scoreboard.startGame("Mexico", "Canada")
        def game2 = scoreboard.startGame("Brazil", "Argentina")
        def game3 = scoreboard.startGame("Spain", "France")

        scoreboard.updateScore(game1, 1, 2)     // total 3
        scoreboard.updateScore(game2, 3, 1)     // total 4
        scoreboard.updateScore(game3, 5, 6)     // total 11

        when:
        def summary = scoreboard.getSummary()

        then:
        summary.size() == 3
        summary == [
                new Game("Spain", "France", 5, 6),
                new Game("Brazil", "Argentina", 3, 1),
                new Game("Mexico", "Canada", 1, 2)
        ]

        and: "games are printed in correct format"
        summary*.toString() == [
                "Spain 5 - France 6",
                "Brazil 3 - Argentina 1",
                "Mexico 1 - Canada 2"
        ]
    }

    def "should return summary ordered by start time desc if total scores are equal"() {
        given:
        clock.instant() >>> [
                TEST_INSTANT,
                TEST_INSTANT.plusSeconds(2),
                TEST_INSTANT.plusSeconds(4)
        ]

        def game1 = scoreboard.startGame("Brazil", "Germany")
        def game2 = scoreboard.startGame("Mexico", "Canada")
        def game3 = scoreboard.startGame("Spain", "France")

        scoreboard.updateScore(game1, 1, 2)     // total 3  @ T
        scoreboard.updateScore(game2, 3, 0)     // total 3  @ T+2
        scoreboard.updateScore(game3, 5, 6)     // total 11 @ T+4

        when:
        def summary = scoreboard.getSummary()

        then:
        summary.size() == 3
        summary == [
                new Game("Spain", "France", 5, 6),      // total 11 @ T+4
                new Game("Mexico", "Canada", 3, 0),     // total 3 @ T+2
                new Game("Brazil", "Germany", 1, 2)     // total 3 @ T
        ]

        and: "games are printed in correct format"
        summary*.toString() == [
                "Spain 5 - France 6",
                "Mexico 3 - Canada 0",
                "Brazil 1 - Germany 2"
        ]
    }

    def "should return summary matching the exact example provided in requirements"() {
        given: "games are started in specific order"
        clock.instant() >>> (1..5).collect { i ->
            TEST_INSTANT.plusSeconds(i)
        }

        def gameIdMexCan = scoreboard.startGame("Mexico", "Canada")
        def gameIdSpaBra = scoreboard.startGame("Spain", "Brazil")
        def gameIdGerFra = scoreboard.startGame("Germany", "France")
        def gameIdUruIta = scoreboard.startGame("Uruguay", "Italy")
        def gameIdArgAus = scoreboard.startGame("Argentina", "Australia")

        and: "Scores are updated to match the example"
        scoreboard.updateScore(gameIdMexCan, 0, 5)     // total 5
        scoreboard.updateScore(gameIdSpaBra, 10, 2)    // total 12
        scoreboard.updateScore(gameIdGerFra, 2, 2)     // total 4
        scoreboard.updateScore(gameIdUruIta, 6, 6)     // total 12
        scoreboard.updateScore(gameIdArgAus, 3, 1)     // total 4

        when:
        def summary = scoreboard.getSummary()

        then:
        summary.size() == 5
        summary*.toString() == [
                "Uruguay 6 - Italy 6",
                "Spain 10 - Brazil 2",
                "Mexico 0 - Canada 5",
                "Argentina 3 - Australia 1",
                "Germany 2 - France 2"
        ]
    }
}
