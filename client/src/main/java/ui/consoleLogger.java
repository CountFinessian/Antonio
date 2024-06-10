package ui;
import exception.DataAccessException;
import responserequest.RegisterRequest;
import server.ServerFacade;
public class consoleLogger {
    private final String Log;

    public consoleLogger(String serverUrl) {
        this.Log = serverUrl;
    }
    public void run() throws DataAccessException {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
        ServerFacade facade = new ServerFacade(Log);
        RegisterRequest chessPerson = new RegisterRequest("KAWILL", "D3cember!", "kman16409@gmail.com");
        facade.createUser(chessPerson);
    }
}
