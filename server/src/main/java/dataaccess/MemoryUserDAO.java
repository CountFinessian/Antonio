package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;


public class MemoryUserDAO implements UserDAO {
    final static private HashMap<String, UserData> users = new HashMap<>();

    public UserData createUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());
        users.put(user.username(), user);
        return user;
    }

    public UserData getUser(String user) {
        return users.get(user);
    }

    public void removeUser(AuthData userAuth) throws DataAccessException {
        users.remove(userAuth.username());
    }
}
