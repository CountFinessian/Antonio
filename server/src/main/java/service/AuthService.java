package service;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import model.AuthData;
import model.UserData;

public class AuthService {
    private final AuthDAO authaccess;

    public AuthService(AuthDAO authaccess) { this.authaccess = authaccess; }

    public AuthData createAuth(UserData user) throws DataAccessException {
        return authaccess.createAuth(user);
    }
    public void logoutAuth(String authToken) throws DataAccessException {
        authaccess.logoutAuth(authToken);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authaccess.getAuth(authToken);
    }
    public void clearAuths() throws DataAccessException {
        authaccess.clearAuths();
    }
}

