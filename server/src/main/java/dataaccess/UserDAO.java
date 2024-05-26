package dataaccess;

import model.*;

public interface UserDAO {
     UserData createUser (UserData user) throws DataAccessException;
     UserData getUser (UserData user) throws DataAccessException;
     void removeUser (AuthData userAuth) throws DataAccessException;
}
