package ru.vsu.g72.players.repository;

import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.domain.Progress;

import java.util.List;

public interface ProgressRepository extends CrudRepository<Progress, Long> {
    List<Progress> findAllProgressesWherePlayerIs(Player player);
}
