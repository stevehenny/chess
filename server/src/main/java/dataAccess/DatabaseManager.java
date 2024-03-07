package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataErrorException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataErrorException(500, e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataErrorException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataErrorException(500, e.getMessage());
        }
    }

    private static final String[] createStatements = {
        "CREATE TABLE IF NOT EXISTS Users (" +
            "username VARCHAR(255) NOT NULL PRIMARY KEY," +
            "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL" +
        ")",
        "CREATE TABLE IF NOT EXISTS Auth (" +
            "authToken VARCHAR(255) PRIMARY KEY," +
            "username VARCHAR(255) NOT NULL" +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci",

        "CREATE TABLE IF NOT EXISTS Games (" +
            "gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "whiteUsername VARCHAR(255)," +
                "blackUsername VARCHAR(255)," +
                "gameName VARCHAR(255) NOT NULL," +
                "game TEXT NOT NULL," +
                "UNIQUE (gameName)" +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci"

    };

    public static void main(String[] args) throws DataErrorException {
        configureDatabase();
    }

    /**
     * Configure the database.
     */
    static void configureDatabase() throws DataErrorException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataErrorException(500, e.getMessage());
        }
    }

}
