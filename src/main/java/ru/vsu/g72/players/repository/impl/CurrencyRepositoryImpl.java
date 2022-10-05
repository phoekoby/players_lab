package ru.vsu.g72.players.repository.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.vsu.g72.players.domain.Currency;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.repository.CurrencyRepository;
import ru.vsu.g72.players.repository.DatabaseConnection;

import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Named
public class CurrencyRepositoryImpl implements CurrencyRepository {
    private final Connection dbConnection = DatabaseConnection.getDbConnection();

    @Override
    public Currency save(Currency currency) {
        if (currency == null || currency.getId() == null) {
            throw new IllegalArgumentException("Impossible save null or with null id entity");
        }
        if (existById(currency.getId())) {
            return update(currency);
        }
        String statement = "INSERT INTO currency (id, player_id, resourceid, name, count) values (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setLong(1, currency.getId());
            preparedStatement.setLong(2, currency.getPlayerId().getId());
            preparedStatement.setLong(3, currency.getResourceId());
            preparedStatement.setString(4, currency.getName());
            preparedStatement.setInt(5, currency.getCount());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Progress failed, no rows affected.");
            }
            return currency;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Currency update(@NonNull Currency currency) {
        String statement = "UPDATE currency SET resourceid=?, name=?, count=? WHERE id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setLong(1, currency.getResourceId());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setInt(3, currency.getCount());
            preparedStatement.setLong(4, currency.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating Progress failed, no rows affected.");
            }
            return currency;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> saveAll(Collection<Currency> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Impossible save null collection");
        }
        return collection.stream().map(this::save).collect(Collectors.toList());
    }

    private Currency createNewCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getLong("id"));
        currency.setName(resultSet.getString("name"));
        currency.setCount(resultSet.getInt("count"));
        currency.setResourceId(resultSet.getLong("resourceid"));
        Player player = new Player();
        player.setId(resultSet.getLong("player_id"));
        player.setNickname(resultSet.getString("nickname"));
        currency.setPlayerId(player);
        return currency;
    }

    @Override
    public Optional<Currency> getById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Currency currency = null;
        String getProgressById = "SELECT * FROM currency Right Join player p on p.id = currency.player_id WHERE currency.id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getProgressById)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    currency = createNewCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(currency);
    }

    @Override
    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        String getAllCurrencies = "SELECT * FROM currency LEFT JOIN player p on p.id = currency.player_id";
        try (Statement statement = dbConnection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(getAllCurrencies)) {
                while (resultSet.next()) {
                    currencies.add(createNewCurrency(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return currencies;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            return;
        }
        String query = "DELETE FROM currency WHERE id=?";
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

    @Override
    public List<Currency> findAllCurrenciesWherePlayerIs(Player player) {
        if (player == null || player.getId() == null) {
            throw new IllegalArgumentException("Player can not be null and his id can not be null");
        }
        List<Currency> currencies = new ArrayList<>();
        String getAllItems = "SELECT * FROM currency LEFT JOIN player p on p.id = currency.player_id WHERE player_id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getAllItems)) {
            preparedStatement.setLong(1, player.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    currencies.add(createNewCurrency(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return currencies;
    }
}
