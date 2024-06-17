package ui;

import chess.*;
import exception.DataAccessException;
import model.AuthData;
import model.GameData;
import responserequest.GetAllGamesResponse;
import responserequest.JoinGameRequest;
import responserequest.MakeGameRequest;
import responserequest.MakeGameResponse;
import server.ServerFacade;

import java.io.IOException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import static ui.EscapeSequences.*;
public class EnterChessGame {
    private static Scanner scanner;
    private static String url;
    private static ServerFacade facade;
    private static AuthData loogyinny;
    private static GamePlay gameplayui;

    public EnterChessGame(String url, AuthData loggyinny) {
        this.url = url;
        this.loogyinny = loggyinny;
        this.scanner = new Scanner(System.in);
        this.facade = new ServerFacade(url);
    }

    public static Boolean postlogin() throws DataAccessException, DataFormatException, IOException, InvalidMoveException {
        boolean mainmenu = true;
        while (mainmenu) {
            System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "Logged in as " + loogyinny.username() + "." + RESET_TEXT_COLOR);
            System.out.println("Please type a command to start playing.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "create":
                    create();
                    break;
                case "join":
                    join();
                    // Implementation for join
                    // Register, then Render Game
                    break;
                case "observe":
                    System.out.println("Please enter the Game ID.");
                    int gameID = Integer.parseInt(scanner.nextLine().trim().toLowerCase());
                    gameplayui = new GamePlay(url, loogyinny, gameID, true);
                    gameplayui.display();
                    // Render Game
                    break;
                case "list":
                    GetAllGamesResponse games = facade.listGames(loogyinny.authToken());
                    int listNum = 0;
                    for (GameData game : games.games()) {
                        listNum++;
                        // Print game details here
                        System.out.println(SET_TEXT_COLOR_YELLOW + listNum + SET_TEXT_COLOR_BLUE +  "." + RESET_TEXT_COLOR);
                        System.out.println(SET_TEXT_COLOR_YELLOW + "Game Name: " + SET_TEXT_COLOR_BLUE + game.gameName() + RESET_TEXT_COLOR);
                        System.out.println(SET_TEXT_COLOR_WHITE + "WHITE: " + SET_TEXT_COLOR_BLUE + game.whiteUsername());
                        System.out.println(SET_TEXT_COLOR_BLACK + "BLACK: " + SET_TEXT_COLOR_BLUE + game.blackUsername() + RESET_TEXT_COLOR);
                        System.out.println("-----------------------------");
                    }
                    break;
                case "logout":
                    System.out.println(SET_TEXT_ITALIC + "Logging out..." + RESET_TEXT_ITALIC);
                    facade.deleteUser(loogyinny.authToken());
                    return true;
                case "quit":
                    return false;
                case "help":
                    System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "[LOGGED_IN] for help:" + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "create " + SET_TEXT_COLOR_BLUE + "<NAME>" + SET_TEXT_COLOR_YELLOW + " - a game," + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "join " + SET_TEXT_COLOR_BLUE + "<ID> <WHITE|BLACK>" + SET_TEXT_COLOR_YELLOW + " - a game" + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "observe " + SET_TEXT_COLOR_BLUE + "<ID>" + SET_TEXT_COLOR_YELLOW + " - a game" + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "list - games" + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "logout - when you are done." + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "quit - playing chess." + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "help - with possible commands." + RESET_TEXT_COLOR);
                    break;
                default:
                    System.out.println(SET_TEXT_COLOR_RED + "Unknown command. Please try again." + RESET_TEXT_COLOR);
                    break;
            }
        }
        return false;
    }

    private static void create() throws DataAccessException {
        System.out.println("Please enter the game name.");
        String gameName = scanner.nextLine().trim().toLowerCase();

        if (gameName.isEmpty()) {
            System.out.println(SET_TEXT_COLOR_RED + "Invalid game name." + RESET_TEXT_COLOR);
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                create();
            }
        }
        MakeGameRequest newGameRequest = new MakeGameRequest(gameName);

        try {
            MakeGameResponse newGame = facade.createGame(newGameRequest, loogyinny.authToken());
            int gameID = (newGame.gameID());
        } catch (DataAccessException e) {
            // Handle exception
        }
    }

    private static void join() throws DataAccessException {
        try {
            System.out.println("Please enter the game number.");
            String gameIDInput = scanner.nextLine().trim().toLowerCase();

            // Check if gameIDInput is not a number
            if (!gameIDInput.matches("\\d+")) {
                throw new DataAccessException("Invalid game ID: Game ID must be a number.");
            }

            int gameID = Integer.parseInt(gameIDInput);
            System.out.println("Please enter B for Black or W for White");
            String gameColorInitial = scanner.nextLine().trim().toLowerCase();

            if (gameColorInitial.equals("b")) {
                gameColorInitial = "BLACK";
            } else if (gameColorInitial.equals("w")) {
                gameColorInitial = "WHITE";
            } else {
                throw new DataAccessException("Invalid gameColorInitial: " + gameColorInitial);
            }
            JoinGameRequest playerToJoin = new JoinGameRequest(gameColorInitial, gameID);
            facade.joinGame(playerToJoin, loogyinny.authToken());
            System.out.println(SET_TEXT_COLOR_YELLOW + loogyinny.username() + " " + SET_TEXT_COLOR_BLUE + "joined the game!" + RESET_TEXT_COLOR);

            gameplayui = new GamePlay(url, loogyinny, gameID, false);
            gameplayui.display();
        } catch (DataAccessException e) {
            System.out.println(SET_TEXT_COLOR_RED + "Invalid game ID or Game Color or GameFull" + RESET_TEXT_COLOR);
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                join();
            }
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }
}
