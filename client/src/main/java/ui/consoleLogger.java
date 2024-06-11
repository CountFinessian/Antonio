package ui;

import exception.DataAccessException;
import model.AuthData;
import responserequest.*;
import server.ServerFacade;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class consoleLogger {
    private String Log = "";
    Scanner scanner = new Scanner(System.in);
    Boolean quitButton = true;

    public consoleLogger(String serverUrl) {
        this.Log = serverUrl;
    }

    public void run() throws DataAccessException {
        ServerFacade facade = new ServerFacade(Log);
        while (quitButton) {
            System.out.println("Hello, please type a command.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "clear":
                    System.out.println(STR."\{SET_TEXT_COLOR_RED}Do you really?\{RESET_TEXT_COLOR}");
                    System.out.println("YES or NO?");
                    String deleteSequence = scanner.nextLine().trim().toLowerCase();
                    if (deleteSequence.equals("yes")) {
                        System.out.println(STR."\{SET_TEXT_ITALIC}Deleting everything...\{RESET_TEXT_ITALIC}");
                        facade.deleteDatabase();
                    }
                    break; // Break added here
                case "login":
                    Login();
                    // Call a method to handle sign-in logic
                    break;
                case "register":
                    Register();
                    break;
                case "quit":
                    System.out.println("Quitting...");
                    return;
                case "help":
                    System.out.println(STR."\{SET_TEXT_COLOR_LIGHT_GREY}[LOGGED_OUT] for help:\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}register \{SET_TEXT_COLOR_BLUE}<USERNAME> <PASSWORD> <EMAIL>\{SET_TEXT_COLOR_YELLOW} - to create an account,\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}login \{SET_TEXT_COLOR_BLUE}<USERNAME> <PASSWORD>\{SET_TEXT_COLOR_YELLOW} - to play chess,\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}quit - playing chess,\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}help - with possible commands.\{RESET_TEXT_COLOR}");
                    System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}clear - delete data.\{RESET_TEXT_COLOR}");
                    break;
                default:
                    System.out.println(STR."\{SET_TEXT_COLOR_RED}Unknown command. Please try again.\{RESET_TEXT_COLOR}");
                    break;
            }
        }
    }

    private void Register() throws DataAccessException {
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
                AuthData LoggedInPlayer = new AuthData(newplayer.authToken(), newplayer.username());
                System.out.println(STR."\{SET_TEXT_ITALIC}Registering...\{RESET_TEXT_ITALIC}");
                quitButton = enterChessGame.PostLogin(LoggedInPlayer, Log);

            } catch (DataAccessException e) {
                System.out.println(STR."\{SET_TEXT_COLOR_RED}Username already taken.\{RESET_TEXT_COLOR}");
                System.out.println("Press any key to try again.");
                System.out.println("Or type exit to go back to main menu");

                String todo = scanner.nextLine().trim().toLowerCase();
                if (!todo.equals("exit")) {
                    Register();
            }
        }
    }

    private void Login() throws DataAccessException {
        ServerFacade facade = new ServerFacade(Log);
        System.out.println("Please enter your username.");
        String username = scanner.nextLine();

        System.out.println("Please enter your password.");
        String password = scanner.nextLine();

        LoginRequest chessPerson = new LoginRequest(username, password);
        System.out.println(STR."\{SET_TEXT_ITALIC}Logging in...\{RESET_TEXT_ITALIC}");

        try {
            LoginResponse LoggedInPlayer = facade.loginUser(chessPerson);
            AuthData loggedInPlayer = new AuthData(LoggedInPlayer.authToken(), LoggedInPlayer.username());
            quitButton = enterChessGame.PostLogin(loggedInPlayer, Log);

        } catch (DataAccessException e) {
            System.out.println(STR."\{SET_TEXT_COLOR_RED}Incorrect Username or Password.\{RESET_TEXT_COLOR}");
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                Login();
            }
        }
    }
}
