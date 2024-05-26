package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

public class UserService {
    private final UserDAO UserAccess;

    public UserService(UserDAO UserAccess) { this.UserAccess = UserAccess; }

    public UserData createUser(UserData user) throws DataAccessException {
        return UserAccess.createUser(user);
    }

    public UserData getUser(UserData user) throws DataAccessException {
        return UserAccess.getUser(user);
    }

    public void removeUser(AuthData userauth) throws DataAccessException {
        UserAccess.removeUser(userauth);
    }
}
