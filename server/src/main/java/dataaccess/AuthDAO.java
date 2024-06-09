package dataaccess;
import exception.DataAccessException;

import model.*;
public interface AuthDAO {
    AuthData createAuth(UserData user) throws DataAccessException;
    void logoutAuth(String authToken) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void clearAuths() throws DataAccessException;
}
