package dataaccess;

import model.AuthData;
import model.UserData;

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

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
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
