package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

import java.util.Random;
import java.util.UUID;


public class MemoryAuthDAO implements AuthDAO {
    final static private HashMap<String, AuthData> AUTHS = new HashMap<>();

    public AuthData createAuth(UserData user) {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        AUTHS.put(authToken, auth);
        return auth;
    }
    public void logoutAuth(String authToken) {
        AUTHS.remove(authToken);
    }
    public AuthData getAuth(String authToken) {
        return AUTHS.get(authToken);
    }
    public void clearAuths () throws DataAccessException {
        AUTHS.clear();
    }
    }


