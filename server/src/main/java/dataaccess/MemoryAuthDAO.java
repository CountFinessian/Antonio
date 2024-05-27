package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

import java.util.Random;
import java.util.UUID;


public class MemoryAuthDAO implements AuthDAO {
    final static private HashMap<String, AuthData> auths = new HashMap<>();

    public AuthData createAuth(UserData user) {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        auths.put(authToken, auth);
        return auth;
    }
    public void  logoutAuth(String authToken) {
        AuthData deletedAuth = auths.get(authToken);
        auths.remove(authToken);
    }
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }
    }


