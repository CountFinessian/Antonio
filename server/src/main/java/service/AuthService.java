package service;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class AuthService {
    private final AuthDAO AuthAccess;

    public AuthService(AuthDAO AuthAccess) { this.AuthAccess = AuthAccess; }

    public AuthData createAuth(UserData user) throws DataAccessException {
        return AuthAccess.createAuth(user);
    }
    public AuthData logoutAuth(String authToken) throws DataAccessException {
        return AuthAccess.logoutAuth(authToken);
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        return AuthAccess.getAuth(authToken);
    }
}

