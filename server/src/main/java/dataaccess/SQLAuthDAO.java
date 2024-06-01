package dataaccess;

import model.AuthData;
import model.UserData;

public class SQLAuthDAO implements AuthDAO{
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
}
