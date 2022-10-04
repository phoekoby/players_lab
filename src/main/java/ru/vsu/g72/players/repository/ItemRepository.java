package ru.vsu.g72.players.repository;

import ru.vsu.g72.players.domain.Item;
import ru.vsu.g72.players.domain.Player;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findAllItemsWherePlayerIs(Player player);
}
