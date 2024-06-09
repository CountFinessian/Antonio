package service;
import dataaccess.UserDAO;
import model.*;
import exception.DataAccessException;

public class UserService {
    private final UserDAO useraccess;

    public UserService(UserDAO useraccess) { this.useraccess = useraccess; }

    public UserData createUser(UserData user) throws DataAccessException {
        return useraccess.createUser(user);
    }

    public UserData getUser(String user) throws DataAccessException {
        return useraccess.getUser(user);
    }

    public void clearUsers() throws DataAccessException {
        useraccess.clearUsers();
    }
}
