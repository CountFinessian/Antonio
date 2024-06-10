package ui;

import exception.DataAccessException;
import responserequest.*;
import server.ServerFacade;
public class consoleLogger {
    private final String Log;

    public consoleLogger(String serverUrl) {
        this.Log = serverUrl;
    }
    public void run() throws DataAccessException {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
        ServerFacade facade = new ServerFacade(Log);
        RegisterRequest new_chessPerson = new RegisterRequest("KAWILL", "D3cember!", "kman16409@gmail.com");
        LoginRequest chessPerson = new LoginRequest("KAWILL", "D3cember!");
        MakeGameRequest gameBoard = new MakeGameRequest("Frozone");
        JoinGameRequest joinGame = new JoinGameRequest("WHITE", 3);
        try {
//            RegisterResponse newplayer = facade.createUser(new_chessPerson);
            LoginResponse player = facade.loginUser(chessPerson);
            MakeGameResponse gameOn = facade.createGame(gameBoard, player.authToken());
            facade.joinGame(joinGame, player.authToken());
            System.out.print(facade.listGames(player.authToken()));
//            facade.deleteDatabase();

//            facade.deleteUser(player.authToken());
            // Handle the player response if necessary
        } catch (DataAccessException player) {
            System.out.print(player.getMessage());
        }
    }
}
