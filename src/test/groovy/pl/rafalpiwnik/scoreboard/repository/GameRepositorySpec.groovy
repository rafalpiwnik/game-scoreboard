package pl.rafalpiwnik.scoreboard.repository

import pl.rafalpiwnik.scoreboard.data.GameData
import spock.lang.Specification
import spock.lang.Subject

import static java.time.Instant.now
import static java.util.UUID.randomUUID

class GameRepositorySpec extends Specification {

    @Subject
    GameRepository gameRepository = new InMemoryGameRepository()

    def "should save and retrieve a game"() {
        given: "a new game"
        def gameId = randomUUID()
        def createTimestamp = now()
        def game = new GameData(gameId, "mexico", "canada", createTimestamp)

        when: "the game is saved"
        gameRepository.save(game)

        then: "the game can be retrieved"
        def retrievedGame = gameRepository.findById(gameId)
        retrievedGame.isPresent()
        verifyAll(retrievedGame.get()) {
            id == gameId
            homeTeam == "mexico"
            awayTeam == "canada"
            startTime == createTimestamp
        }
    }

    def "should update existing game"() {
        given: "saved game with 0-0 score"
        def gameId = randomUUID()
        def game = new GameData(gameId, "mexico", "canada", now())

        when: "game score is changed and the game is saved"
        game.setScores(3, 5)
        gameRepository.save(game)

        then: "retrieved game has updated score"
        def retrievedGame = gameRepository.findById(gameId)
        retrievedGame.isPresent()
        verifyAll(retrievedGame.get()) {
            id == gameId
            homeTeam == "mexico"
            awayTeam == "canada"
            startTime == game.startTime
            homeScore == 3
            awayScore == 5
        }
    }

    def "should delete game by id"() {
        given:
        def gameId = randomUUID()
        gameRepository.save(new GameData(gameId, "mexico", "canada", now()))

        expect:
        gameRepository.findById(gameId).isPresent()

        when:
        gameRepository.deleteById(gameId)

        then:
        gameRepository.findById(gameId).isEmpty()
    }

    def "findAll should return empty collection when no games are saved"() {
        when:
        def allGames = gameRepository.findAll()

        then:
        allGames.isEmpty()
    }

    def "findAll should return all games"() {
        given:
        def referenceTime = now()
        def games = [
                new GameData(randomUUID(), "mexico", "canada", referenceTime),
                new GameData(randomUUID(), "brazil", "argentina", referenceTime.plusSeconds(1)),
                new GameData(randomUUID(), "spain", "france", referenceTime.plusSeconds(2)),
        ]
        games.each {
            gameRepository.save(it)
        }

        when:
        def allGames = gameRepository.findAll()

        then:
        allGames.size() == games.size()
        allGames.containsAll(games)
    }
}
