package dataaccess;

import model.*;

public interface UserDAO {
     UserData createUser (UserData user) throws DataAccessException;
     UserData getUser (String user) throws DataAccessException;
     void removeUser (AuthData userAuth) throws DataAccessException;
}
