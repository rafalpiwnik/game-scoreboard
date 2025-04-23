package pl.rafalpiwnik.scoreboard.service

import pl.rafalpiwnik.scoreboard.repository.GameRepository
import pl.rafalpiwnik.scoreboard.repository.InMemoryGameRepository
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.time.Clock
import java.time.Instant

class ScoreBoardServiceSpec extends Specification {

    @Shared
    Clock clock = Mock(Clock)

    GameRepository gameRepository = new InMemoryGameRepository()

    @Subject
    ScoreBoard scoreboard = new ScoreBoardService(gameRepository, clock)

    private final static Instant TEST_INSTANT = Instant.now()

    def setupSpec() {
        clock.instant() >> TEST_INSTANT
    }

    def "should start game"() {
        given: "a game with home and away teams"
        def homeTeam = "Mexico"
        def awayTeam = "Canada"

        when: "the game is started"
        def gameId = scoreboard.startGame(homeTeam, awayTeam)

        then:
        def retrievedGame = gameRepository.findById(gameId)
        retrievedGame.isPresent()
        verifyAll(retrievedGame.get()) {
            id == gameId
            homeTeam == homeTeam
            awayTeam == awayTeam
            startTime == TEST_INSTANT
        }
    }

    def "should finish game"() {
        given: "a game with home and away teams"
        def gameId = scoreboard.startGame("Mexico", "Canada")

        when: "the game is finished"
        scoreboard.finishGame(gameId)

        then:
        def retrievedGame = gameRepository.findById(gameId)
        retrievedGame.isPresent()
        verifyAll(retrievedGame.get()) {
            id == gameId
            end
        }
    }

    def "FinishGame"() {
    }

    def "UpdateScore"() {
    }

    def "GetSummary"() {
    }
}
