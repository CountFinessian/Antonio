package dataaccess;

import model.*;
public interface AuthDAO {
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData logoutAuth(String authToken) throws DataAccessException;
}
