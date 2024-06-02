package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        SQLUserDAO.executeUpdate(statement, authToken, user.username());
        AuthData auth = new AuthData(authToken, user.username());
        return auth;

    }

    @Override
    public void logoutAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM AuthData WHERE authToken=?";
        SQLUserDAO.executeUpdate(statement, authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM AuthData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String authToken2 = rs.getString("authToken");
                        String username2 = rs.getString("username");
                        return new AuthData(authToken2, username2);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }


    @Override
    public void clearAuths() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
    CREATE TABLE IF NOT EXISTS AuthData (
        authToken VARCHAR(256) NOT NULL,
        username VARCHAR(256) NOT NULL,
        PRIMARY KEY (authToken)
    )"""
    };

    private void configureDatabase() throws DataAccessException {
        SQLUserDAO.databaseConfigure(createStatements);
    }
}
