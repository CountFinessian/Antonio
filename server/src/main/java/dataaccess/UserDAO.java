package dataaccess;
import exception.DataAccessException;
import model.*;

public interface UserDAO {
     UserData createUser (UserData user) throws DataAccessException;
     UserData getUser (String user) throws DataAccessException;
     void clearUsers () throws DataAccessException;
}
