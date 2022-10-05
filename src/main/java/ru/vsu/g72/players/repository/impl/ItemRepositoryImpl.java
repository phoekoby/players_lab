package ru.vsu.g72.players.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.vsu.g72.players.domain.Item;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.repository.DatabaseConnection;
import ru.vsu.g72.players.repository.ItemRepository;

import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Named
public class ItemRepositoryImpl implements ItemRepository {
    private final Connection dbConnection = DatabaseConnection.getDbConnection();

    @Override
    public Item save(Item item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException("Impossible save null or with null id entity");
        }
        if(existById(item.getId())){
            return update(item);
        }
        String statement = "INSERT INTO ITEM (id, player_id, resourceid, count, level) values (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setLong(1, item.getId());
            preparedStatement.setLong(2, item.getPlayerId().getId());
            preparedStatement.setLong(3, item.getResourceId());
            preparedStatement.setInt(4, item.getCount());
            preparedStatement.setInt(5, item.getLevel());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Item failed, no rows affected.");
            }
            return item;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> saveAll(Collection<Item> items) {
        if (items == null) {
            throw new IllegalArgumentException("Impossible save null collection");
        }
        return items.stream().map(this::save).collect(Collectors.toList());
    }

    private Item update(Item item) {
        String statement = "UPDATE item SET resourceid=?, count=?, level=? WHERE id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setLong(1, item.getResourceId());
            preparedStatement.setInt(2, item.getCount());
            preparedStatement.setInt(3, item.getLevel());
            preparedStatement.setLong(4, item.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating Item failed, no rows affected.");
            }
            return item;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Item> getById(Long id) {
        if(id==null){
            return Optional.empty();
        }
        Item item = null;
        String getItemById = "SELECT * FROM item i Right Join player p on p.id = i.player_id WHERE i.id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getItemById)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    item = createItem(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(item);
    }

    private Item createItem(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setId(resultSet.getLong("id"));
        item.setCount(resultSet.getInt("count"));
        item.setLevel(resultSet.getInt("level"));
        item.setResourceId(resultSet.getLong("resourceid"));
        Player player = new Player();
        player.setId(resultSet.getLong("player_id"));
        player.setNickname(resultSet.getString("nickname"));
        item.setPlayerId(player);
        return item;
    }

    @Override
    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        String getAllItems = "SELECT * FROM item i LEFT JOIN player p on p.id = i.player_id";
        try (Statement statement = dbConnection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(getAllItems)) {
                while (resultSet.next()) {
                    items.add(createItem(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return items;
    }

    @Override
    public void delete(Long id) {
        if (id == null){
            return;
        }
        String query = "DELETE FROM item WHERE id=?";
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
    public List<Item> findAllItemsWherePlayerIs(Player player) {
        if(player == null || player.getId() == null){
            throw new IllegalArgumentException("Player can not be null and his id can not be null");
        }
        List<Item> items = new ArrayList<>();
        String getAllItems = "SELECT * FROM item i LEFT JOIN player p on p.id = i.player_id WHERE i.player_id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getAllItems)) {
            preparedStatement.setLong(1, player.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(createItem(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return items;
    }
}
