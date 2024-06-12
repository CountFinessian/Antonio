package client;

import exception.DataAccessException;
import org.junit.jupiter.api.*;
import responserequest.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on port " + port);
        String serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
    }

    @BeforeEach
    public void before() throws DataAccessException {
        facade.deleteDatabase();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void createUserPos() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        assert(createdUser.username()).equals("JAWILL");
    }

    @Test
    public void createUserNeg() throws DataAccessException {
        RegisterRequest newUser2 = new RegisterRequest(null, null, null);
        var nullUser = assertThrows(DataAccessException.class, () -> facade.createUser(newUser2));
        assertEquals("failure: 400", nullUser.getMessage());
    }

    @Test
    public void loginUserPos() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        facade.deleteUser(createdUser.authToken());
        LoginRequest newLogin = new LoginRequest("JAWILL", "D3cember!");
        LoginResponse loginData = facade.loginUser(newLogin);
        assert(loginData.username().equals("JAWILL"));
    }

    @Test
    public void loginUserNeg() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        facade.deleteUser(createdUser.authToken());
        LoginRequest newLogin = new LoginRequest(null, "D3cember!");
        var nullUser = assertThrows(DataAccessException.class, () -> facade.loginUser(newLogin));
        assertEquals("failure: 401", nullUser.getMessage());
    }

    @Test
    public void logoutUserPos() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        facade.deleteUser(createdUser.authToken());
        MakeGameRequest testGame = new MakeGameRequest("Krazy Kid");
        var nullGame = assertThrows(DataAccessException.class, () -> facade.createGame(testGame, createdUser.authToken()));
        assertEquals("failure: 401", nullGame.getMessage());
    }

    @Test
    public void logoutUserNeg() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        var nullLogout = assertThrows(DataAccessException.class, () -> facade.deleteUser(createdUser.username()));
        assertEquals("failure: 401", nullLogout.getMessage());
    }

    @Test
    public void createGamePos() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        MakeGameRequest testGame = new MakeGameRequest("Krazy Kid");
        MakeGameResponse newGame = facade.createGame(testGame, createdUser.authToken());
        assertEquals(1, newGame.gameID());
    }

    @Test
    public void createGameNeg() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        MakeGameRequest testGame = new MakeGameRequest(null);
        var nullGame = assertThrows(DataAccessException.class, () -> facade.createGame(testGame, createdUser.authToken()));
        assertEquals("failure: 500", nullGame.getMessage());
    }

    @Test
    public void listGamesPos() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        MakeGameRequest testGame = new MakeGameRequest("Krazy Kid");
        MakeGameResponse newGame = facade.createGame(testGame, createdUser.authToken());
        GetAllGamesResponse myGames = facade.listGames(createdUser.authToken());
        assertEquals(newGame.gameID(), myGames.games().listIterator(0).next().gameID());
    }

    @Test
    public void listGamesNeg() throws DataAccessException {
        var nullList = assertThrows(DataAccessException.class, () -> facade.listGames(null));
        assertEquals("failure: 401", nullList.getMessage());
    }

    @Test
    public void joinGamePos() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        MakeGameRequest testGame = new MakeGameRequest("Krazy Kid");
        MakeGameResponse newGame = facade.createGame(testGame, createdUser.authToken());
        JoinGameRequest joinGame = new JoinGameRequest("WHITE", newGame.gameID());
        facade.joinGame(joinGame, createdUser.authToken());
        GetAllGamesResponse myGames = facade.listGames(createdUser.authToken());
        assertEquals(createdUser.username(), myGames.games().listIterator(0).next().whiteUsername());
    }

    @Test
    public void joinGameNeg() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        MakeGameRequest testGame = new MakeGameRequest("Krazy Kid");
        MakeGameResponse newGame = facade.createGame(testGame, createdUser.authToken());
        JoinGameRequest joinGame = new JoinGameRequest(null, newGame.gameID());
        var nullList = assertThrows(DataAccessException.class, () -> facade.joinGame(joinGame, createdUser.authToken()));
        assertEquals("failure: 400", nullList.getMessage());
    }

    @Test
    public void deleteDatabase() throws DataAccessException {
        RegisterRequest newUser = new RegisterRequest("JAWILL", "D3cember!", "jawoba004@gmail.com");
        RegisterResponse createdUser = facade.createUser(newUser);
        facade.deleteDatabase();
        LoginRequest newLogin = new LoginRequest(createdUser.username(), "D3cember!");
        var nullUser = assertThrows(DataAccessException.class, () -> facade.loginUser(newLogin));
        assertEquals("failure: 401", nullUser.getMessage());
    }
}
