package pl.rafalpiwnik.scoreboard.repository;

import pl.rafalpiwnik.scoreboard.data.GameData;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryGameRepository implements GameRepository {

    private final Map<UUID, GameData> games = new ConcurrentHashMap<>();

    @Override
    public void save(GameData gameData) {
        games.put(gameData.getId(), gameData);
    }

    @Override
    public Optional<GameData> findById(UUID id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        return Optional.ofNullable(games.remove(id))
                .isPresent();
    }

    @Override
    public Collection<GameData> findAll() {
        return List.copyOf(games.values());

    }
}
