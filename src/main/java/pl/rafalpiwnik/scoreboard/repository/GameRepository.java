package pl.rafalpiwnik.scoreboard.repository;

import pl.rafalpiwnik.scoreboard.data.GameData;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository {

    void save(GameData gameData);

    Optional<GameData> findById(UUID id);

    boolean deleteById(UUID id);

    Collection<GameData> findAll();
}
