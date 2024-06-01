package dataaccess;

import com.google.gson.Gson;
import model.*;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        databaseConfigure(createStatements);
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String user) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUsers() throws DataAccessException {

    }
    private final String[] createStatements = {
            """
    CREATE TABLE IF NOT EXISTS UserData (
        username VARCHAR(256) NOT NULL,
        password VARCHAR(256) NOT NULL,
        email VARCHAR(256) NOT NULL,
        PRIMARY KEY (username)
    )"""
    };

    static void databaseConfigure(String[] createStatements) throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage());
        }
    }
}
