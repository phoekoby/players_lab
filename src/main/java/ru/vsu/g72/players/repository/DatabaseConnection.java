package ru.vsu.g72.players.repository;


import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DatabaseConnection {

    private static final String DB_DRIVER = "org.postgresql.Driver";

    private static final String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/game";

    private static final String DB_USER = "admin";

    private static final String DB_PASSWORD = "admin";

    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getDbConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            log.debug("Successful connection to DB");
            return connection;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return connection;
    }
}
