package ui;

import exception.DataAccessException;
import model.AuthData;
import responserequest.*;
import server.ServerFacade;
import java.util.Scanner;

public class consoleLogger {
    private String Log = "";
    Scanner scanner = new Scanner(System.in);

    public consoleLogger(String serverUrl) {
        this.Log = serverUrl;
    }

    public void run() throws DataAccessException {
        ServerFacade facade = new ServerFacade(Log);
        while (true) {
            System.out.println("Hello, please enter a command.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "clear":
                    System.out.println("Are you sure you want to do this?");
                    System.out.println("YES or NO?");
                    String deleteSequence = scanner.nextLine().trim().toLowerCase();
                    if (deleteSequence.equals("yes")) {
                        System.out.println("Deleting everything...");
                        facade.deleteDatabase();
                    }
                    break; // Break added here
                case "login":
                    Login();
                    // Call a method to handle sign-in logic
                    break; // Break added here
                case "register":
                    System.out.println("Registering...");
                    Register();
                    break; // Break added here
                case "quit":
                    System.out.println("Quitting...");
                    return;
                case "help":
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY}[LOGGED_OUT] for help:\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}register \{EscapeSequences.SET_TEXT_COLOR_BLUE}<USERNAME> <PASSWORD> <EMAIL>\{EscapeSequences.SET_TEXT_COLOR_YELLOW} - to create an account,\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}login \{EscapeSequences.SET_TEXT_COLOR_BLUE}<USERNAME> <PASSWORD>\{EscapeSequences.SET_TEXT_COLOR_YELLOW} - to play chess,\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}quit - playing chess,\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}help - with possible commands.\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}clear - delete data.\{EscapeSequences.RESET_TEXT_COLOR}");
                    break;
                default:
                    System.out.println("Unknown command. Please try again.");
                    break;
            }
        }
    }

    private void Register() throws DataAccessException {
        ServerFacade facade = new ServerFacade(Log);
        boolean main_menu = true;
        while (main_menu) {
            System.out.println("Please enter your username.");
            String username = scanner.nextLine();

            System.out.println("Please enter your password.");
            String password = scanner.nextLine();

            System.out.println("Please enter your email.");
            String email = scanner.nextLine();

            RegisterRequest new_chessPerson = new RegisterRequest(username, password, email);
            try {
                RegisterResponse newplayer = facade.createUser(new_chessPerson);
                main_menu = false;
                // Go to the Login screen using the AuthData
                AuthData LoggedInPlayer = new AuthData(newplayer.username(), newplayer.username());
                Postlogin(LoggedInPlayer);
                return;
            } catch (Exception e) {
                System.out.println("Username already taken.");
                System.out.println("Press any key to try again.");
                System.out.println("Or type quit to go back to main menu");
                String todo = scanner.nextLine().trim().toLowerCase();
                if (todo.equals("quit")) {
                    main_menu = false;
                }
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

        try {
            LoginResponse LoggedInPlayer = facade.loginUser(chessPerson);
            AuthData authData = new AuthData(LoggedInPlayer.authToken(), LoggedInPlayer.username());
            Postlogin(authData);
        } catch (Exception e) {
            System.out.println("Incorrect Username or Password.");
            System.out.println("Press any key to try again.");
            System.out.println("Or type quit to go back to main menu");
            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("quit")) {
                Login();
            }

        }
    }

    private void Postlogin(AuthData loogyinny) throws DataAccessException {
        ServerFacade facade = new ServerFacade(Log);
        boolean main_menu = true;
        while (main_menu) {
            System.out.println(STR."Logged in as \{loogyinny.username()}.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "create":
                    // Implementation for create
                    break;
                case "join":
                    // Implementation for join
                    break;
                case "observe":
                    // Implementation for observe
                    break;
                case "list":
                    // Implementation for list
                    break;
                case "logout":
                    return;
                case "quit":
                    return;
                case "help":
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY}[LOGGED_IN] for help:\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}create \{EscapeSequences.SET_TEXT_COLOR_BLUE}<NAME>\{EscapeSequences.SET_TEXT_COLOR_YELLOW} - a game,\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}join \{EscapeSequences.SET_TEXT_COLOR_BLUE}<ID> <WHITE|BLACK>\{EscapeSequences.SET_TEXT_COLOR_YELLOW} - a game\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}observe \{EscapeSequences.SET_TEXT_COLOR_BLUE}<ID>\{EscapeSequences.SET_TEXT_COLOR_YELLOW} - a game\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}list - games\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}logout - when you are done.\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}quit - playing chess.\{EscapeSequences.RESET_TEXT_COLOR}");
                    System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}help - with possible commands.\{EscapeSequences.RESET_TEXT_COLOR}");
                    break;
                default:
                    System.out.println("Unknown command. Please try again.");
                    break;
            }
        }
    }
}
