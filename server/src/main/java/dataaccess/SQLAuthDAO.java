package dataaccess;

import model.AuthData;
import model.UserData;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException {
        return null;
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
