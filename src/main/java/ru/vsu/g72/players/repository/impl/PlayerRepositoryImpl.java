package ru.vsu.g72.players.repository.impl;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.vsu.g72.players.domain.Currency;
import ru.vsu.g72.players.domain.Item;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.domain.Progress;
import ru.vsu.g72.players.repository.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ApplicationScoped
@Named
@Default
public class PlayerRepositoryImpl implements PlayerRepository {

    private final Connection dbConnection = DatabaseConnection.getDbConnection();
    @Inject
    private ItemRepository itemRepository;
    @Inject
    private CurrencyRepository currencyRepository;
    @Inject
    private ProgressRepository progressRepository;
    @Override
    public List<Player> saveAll(Collection<Player> player) {
        if (player == null) {
            throw new IllegalArgumentException("Impossible save null collection");
        }
        return player.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public Player save(Player player) {
        if (player == null || player.getId() == null) {
            throw new IllegalArgumentException("Impossible save null or with null id entity");
        }
        if (existById(player.getId())) {
            return update(player);
        }
        String statement = "INSERT INTO PLAYER (id, nickname) values (?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setLong(1, player.getId());
            preparedStatement.setString(2, player.getNickname());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Player failed, no rows affected.");
            }
            cascadeSave(player);
//            player.setCurrencies(currencyRepository.saveAll(player.getCurrencies()));
//            player.setItems(itemRepository.saveAll(player.getItems()));
//            player.setProgresses(progressRepository.saveAll(player.getProgresses()));
            return player;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Player update(@NonNull Player player) {
        String statement = "UPDATE player SET nickname=? WHERE id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setString(1, player.getNickname());
            preparedStatement.setLong(2, player.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating Player failed, no rows affected.");
            }
            cascadeSave(player);
            return player;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Player> getById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Player player = null;
        String getPlayerById = "SELECT id, nickname FROM PLAYER WHERE id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getPlayerById)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    player = Player.builder().build();
                    player.setId(resultSet.getLong("id"));
                    player.setNickname(resultSet.getString("nickname"));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(player);
    }

    @Override
    public List<Player> getAll() {
        List<Player> players = new ArrayList<>();
        String queryGetAllPlayers = "SELECT * FROM PLAYER";
        try (Statement statement = dbConnection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(queryGetAllPlayers)) {
                while (resultSet.next()) {
                    Player player = Player.builder().build();
                    player.setId(resultSet.getLong("id"));
                    player.setNickname(resultSet.getString("nickname"));
                    player.setItems(itemRepository.findAllItemsWherePlayerIs(player));
                    player.setProgresses(progressRepository.findAllProgressesWherePlayerIs(player));
                    player.setCurrencies(currencyRepository.findAllCurrenciesWherePlayerIs(player));
                    players.add(player);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return players;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }
        String query = "DELETE FROM player WHERE id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existById(Long id) {
        return getById(id).isPresent();
    }

    private void cascadeSave(Player player) {
        if (player.getItems() != null) {
            player.setItems(cascadeSaveItem(player.getItems(), player));
        }
        if (player.getProgresses() != null) {
            player.setProgresses(cascadeSaveProgresses(player.getProgresses(), player));
        }
        if (player.getCurrencies() != null) {
            player.setCurrencies(cascadeSaveCurrencies(player.getCurrencies(), player));
        }
    }

    private List<Item> cascadeSaveItem(@NonNull List<Item> items, @NonNull Player player) {
        List<Item> itemsInDb = itemRepository.findAllItemsWherePlayerIs(player);
        itemsInDb
                .stream()
                .filter(item -> !items.contains(item))
                .forEach(item -> itemRepository.delete(item.getId()));
        return itemRepository.saveAll(items);
    }

    private List<Progress> cascadeSaveProgresses(@NonNull List<Progress> progresses, @NonNull Player player) {
        List<Progress> progressesInDb = progressRepository.findAllProgressesWherePlayerIs(player);
        progressesInDb
                .stream()
                .filter(progress -> !progresses.contains(progress))
                .forEach(progress -> progressRepository.delete(progress.getId()));
        return progressRepository.saveAll(progresses);
    }

    private List<Currency> cascadeSaveCurrencies(@NonNull List<Currency> currencies, @NonNull Player player) {
        List<Currency> currenciesInDb = currencyRepository.findAllCurrenciesWherePlayerIs(player);
        currenciesInDb
                .stream()
                .filter(currency -> !currencies.contains(currency))
                .forEach(currency -> currencyRepository.delete(currency.getId()));
        return currencyRepository.saveAll(currencies);
    }
}
