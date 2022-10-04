package ru.vsu.g72.players.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.domain.Progress;
import ru.vsu.g72.players.repository.DatabaseConnection;
import ru.vsu.g72.players.repository.ProgressRepository;

import javax.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Named
public class ProgressRepositoryImpl implements ProgressRepository {
    private final Connection dbConnection = DatabaseConnection.getDbConnection();

    @Override
    public Progress save(Progress progress) {
        if (progress == null || progress.getId() == null) {
            throw new IllegalArgumentException("Impossible save null or with null id entity");
        }
        if(existById(progress.getId())){
            return update(progress);
        }
        String statement = "INSERT INTO PROGRESS (id, player_id, resourceid, score, max_score) values (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setLong(1, progress.getId());
            preparedStatement.setLong(2, progress.getPlayerId().getId());
            preparedStatement.setLong(3, progress.getResourceId());
            preparedStatement.setInt(4, progress.getScore());
            preparedStatement.setInt(5, progress.getMaxScore());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Progress failed, no rows affected.");
            }
            return progress;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Progress> saveAll(Collection<Progress> progresses) {
        if (progresses == null) {
            throw new IllegalArgumentException("Impossible save null collection");
        }
        return progresses.stream().map(this::save).collect(Collectors.toList());
    }


    private Progress update(Progress progress) {
        String statement = "UPDATE progress SET resourceid=?, score=?, max_score=? WHERE id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(statement)) {
            preparedStatement.setLong(1, progress.getResourceId());
            preparedStatement.setInt(2, progress.getScore());
            preparedStatement.setInt(3, progress.getMaxScore());
            preparedStatement.setLong(4, progress.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating Progress failed, no rows affected.");
            }
            return progress;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Progress> getById(Long id) {
        if(id==null){
            return Optional.empty();
        }
        Progress progress = null;
        String getProgressById = "SELECT * FROM progress Right Join player p on p.id = progress.player_id WHERE progress.id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getProgressById)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    progress = createNewProgress(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(progress);
    }

    private Progress createNewProgress(ResultSet resultSet) throws SQLException {
        Progress progress = new Progress();
        progress.setId(resultSet.getLong("id"));
        progress.setScore(resultSet.getInt("score"));
        progress.setMaxScore(resultSet.getInt("max_score"));
        progress.setResourceId(resultSet.getLong("resourceid"));
        Player player = new Player();
        player.setId(resultSet.getLong("player_id"));
        player.setNickname(resultSet.getString("nickname"));
        progress.setPlayerId(player);
        return progress;
    }

    @Override
    public List<Progress> getAll() {
        List<Progress> progresses = new ArrayList<>();
        String getAllItems = "SELECT * FROM progress LEFT JOIN player p on p.id = progress.player_id";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getAllItems)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    progresses.add(createNewProgress(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return progresses;
    }

    @Override
    public void delete(Long id) {
        if (id == null){
            return;
        }
        String query = "DELETE FROM progress WHERE id=?";
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
    public List<Progress> findAllProgressesWherePlayerIs(Player player) {
        if(player == null || player.getId() == null){
            throw new IllegalArgumentException("Player can not be null and his id can not be null");
        }
        List<Progress> progresses = new ArrayList<>();
        String getAllItems = "SELECT * FROM progress LEFT JOIN player p on p.id = progress.player_id WHERE player_id=?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(getAllItems)) {
            preparedStatement.setLong(1, player.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    progresses.add(createNewProgress(resultSet));
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return progresses;
    }
}

