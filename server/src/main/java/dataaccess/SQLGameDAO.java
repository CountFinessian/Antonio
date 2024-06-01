package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> getAllGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public Boolean joinGame(String username, String color, Integer gameId) {
        return null;
    }

    @Override
    public void clearGames() throws DataAccessException {

    }

    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS GameData (
            `gameID` INT NOT NULL AUTO_INCREMENT,
            `whiteUsername` VARCHAR(256) NOT NULL,
            `blackUsername` VARCHAR(256) NOT NULL,
            `gameName` VARCHAR(256) NOT NULL,
            `game` TEXT NOT NULL,
            PRIMARY KEY (`gameID`),
            INDEX(`whiteUsername`),
            INDEX(`blackUsername`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage(), ex);
        }
    }
}
