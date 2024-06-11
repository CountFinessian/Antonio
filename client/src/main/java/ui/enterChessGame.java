package ui;

import exception.DataAccessException;
import model.*;
import responserequest.GetAllGamesResponse;
import responserequest.RegisterRequest;
import responserequest.RegisterResponse;
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
                    Create();
                    // Implementation for create
                    break;
                case "join":
                    Join();
                    // Implementation for join
                    break;
                case "observe":
                    // Implementation for observe
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
                        break;
                    }
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
                    System.out.println("Unknown command. Please try again.");
                    break;
            }
        }

        return false;
    }

    private void Create() throws DataAccessException {
        ServerFacade facade = new ServerFacade(Log);
        boolean main_menu = true;
        System.out.println("Please enter your username.");
        String username = scanner.nextLine();

        System.out.println("Please enter your password.");
        String password = scanner.nextLine();

        System.out.println("Please enter your email.");
        String email = scanner.nextLine();

        RegisterRequest new_chessPerson = new RegisterRequest(username, password, email);
        try {
            RegisterResponse newplayer = facade.createUser(new_chessPerson);
            AuthData LoggedInPlayer = new AuthData(newplayer.username(), newplayer.username());
            System.out.println(STR."\{SET_TEXT_ITALIC}Registering...\{RESET_TEXT_ITALIC}");
            quitButton = enterChessGame.PostLogin(LoggedInPlayer, Log);

        } catch (Exception e) {
            System.out.println(STR."\{SET_TEXT_COLOR_RED}Username already taken.\{RESET_TEXT_COLOR}");
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                Register();
            }
        }
    }
    }
}
