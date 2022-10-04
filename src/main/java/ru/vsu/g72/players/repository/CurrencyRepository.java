package ru.vsu.g72.players.repository;

import ru.vsu.g72.players.domain.Currency;
import ru.vsu.g72.players.domain.Player;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Long>{
    List<Currency> findAllCurrenciesWherePlayerIs(Player player);
}
