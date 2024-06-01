package dataaccess;

import com.google.gson.Gson;
import model.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String user) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUsers() throws DataAccessException {

    }
}
