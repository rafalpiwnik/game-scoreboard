package pl.rafalpiwnik.scoreboard.repository;

import pl.rafalpiwnik.scoreboard.data.GameData;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryGameRepository implements GameRepository {

    @Override
    public void save(GameData gameData) {

    }

    @Override
    public Optional<GameData> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Collection<GameData> findAll() {
        return List.of();
    }
}
