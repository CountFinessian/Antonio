import chess.*;
import exception.DataAccessException;
import ui.*;
import server.ServerFacade;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new consoleLogger(serverUrl).run();
    }

}