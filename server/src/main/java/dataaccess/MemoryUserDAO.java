package dataaccess;

import model.UserData;

import java.util.HashMap;


public class MemoryUserDAO implements UserDAO {
    final static private HashMap<String, UserData> USERS = new HashMap<>();

    public UserData createUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());
        USERS.put(user.username(), user);
        return user;
    }

    public UserData getUser(String user) {
        return USERS.get(user);
    }

    public void clearUsers () throws DataAccessException {
        USERS.clear();
    }
}
