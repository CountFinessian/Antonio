package ui;

import exception.DataAccessException;
import model.*;
import responserequest.*;
import server.ServerFacade;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class enterChessGame {
    static Scanner scanner = new Scanner(System.in);

    public static Boolean PostLogin(AuthData loogyinny, String URL) throws DataAccessException {
        ServerFacade facade = new ServerFacade(URL);
        boolean main_menu = true;
        while (main_menu) {
            System.out.println(STR."\{SET_TEXT_COLOR_LIGHT_GREY}Logged in as \{loogyinny.username()}.\{RESET_TEXT_COLOR}");
            System.out.println(STR."Please type a command to start playing.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "create":
                    Create(URL, loogyinny.authToken());
                    break;
                case "join":
//                    Join(URL, loogyinny.authToken());
                    // Implementation for join
                    //Register, then Render Game
                    break;
                case "observe":
//                    Observe(URL, loogyinny.authToken());
                    // Render Game
                    break;
                case "list":
                    GetAllGamesResponse games = facade.listGames(loogyinny.authToken());
                    for (GameData game : games.games()) {
                        // Print game details here
                        System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}Game ID: \{SET_TEXT_COLOR_BLUE}\{game.gameID()}\{RESET_TEXT_COLOR}");
                        System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}Game Name: \{SET_TEXT_COLOR_BLUE}\{game.gameName()}\{RESET_TEXT_COLOR}");
                        System.out.println(STR."\{SET_TEXT_COLOR_WHITE}WHITE: \{SET_TEXT_COLOR_BLUE}\{game.whiteUsername()}");
                        System.out.println(STR."\{SET_TEXT_COLOR_BLACK}BLACK: \{SET_TEXT_COLOR_BLUE}\{game.blackUsername()}\{RESET_TEXT_COLOR}");
                        System.out.println("-----------------------------");
                    }
                    break;
                case "logout":
                    System.out.println(STR."\{SET_TEXT_ITALIC}Logging out...\{RESET_TEXT_ITALIC}");
                    facade.deleteUser(loogyinny.authToken());
                    return true;
                case "quit":
                    return false;
                case "help":
                    System.out.println(STR."\{SET_TEXT_COLOR_LIGHT_GREY}[LOGGED_IN] for help:\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}create \{SET_TEXT_COLOR_BLUE}<NAME>\{SET_TEXT_COLOR_YELLOW} - a game,\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}join \{SET_TEXT_COLOR_BLUE}<ID> <WHITE|BLACK>\{SET_TEXT_COLOR_YELLOW} - a game\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}observe \{SET_TEXT_COLOR_BLUE}<ID>\{SET_TEXT_COLOR_YELLOW} - a game\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}list - games\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}logout - when you are done.\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}quit - playing chess.\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}help - with possible commands.\{RESET_TEXT_COLOR}");
                    break;
                default:
                    System.out.println(STR."\{SET_TEXT_COLOR_RED}Unknown command. Please try again.\{RESET_TEXT_COLOR}");
                    break;
            }
        }
        return false;
    }

    private static void Create(String URL, String authToken) throws DataAccessException {
        ServerFacade facade = new ServerFacade(URL);

        System.out.println("Please enter the game name.");
        String gameName = scanner.nextLine().trim().toLowerCase();

        if (gameName.isEmpty()){
            System.out.println(STR."\{SET_TEXT_COLOR_RED}Invalid game name.\{RESET_TEXT_COLOR}");
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                Create(URL, authToken);
            }
        }
        MakeGameRequest newGameRequest = new MakeGameRequest(gameName);

        try {
            MakeGameResponse newGame = facade.createGame(newGameRequest, authToken);
            int gameID = newGame.gameID();
            System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}Game ID: \{SET_TEXT_COLOR_BLUE}\{gameID}\{RESET_TEXT_COLOR}");
        } catch (DataAccessException e) {
        }
    }
}

