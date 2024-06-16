package ui;

import exception.DataAccessException;
import model.AuthData;
import responserequest.LoginRequest;
import responserequest.LoginResponse;
import responserequest.RegisterRequest;
import responserequest.RegisterResponse;
import server.ServerFacade;

import java.io.IOException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import static ui.EscapeSequences.*;

public class ConsoleLogger {
    private String log = "";
    Scanner scanner = new Scanner(System.in);
    Boolean quitButton = true;

    public ConsoleLogger(String serverUrl) {
        this.log = serverUrl;
    }

    public void run() throws DataAccessException {
        ServerFacade facade = new ServerFacade(log);
        while (quitButton) {
            System.out.println("Hello, please type a command.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "clear":
                    System.out.println(SET_TEXT_COLOR_RED + "Do you really?" + RESET_TEXT_COLOR);
                    System.out.println("YES or NO?");
                    String deleteSequence = scanner.nextLine().trim().toLowerCase();
                    if (deleteSequence.equals("yes")) {
                        System.out.println(SET_TEXT_ITALIC + "Deleting everything..." + RESET_TEXT_ITALIC);
                        facade.deleteDatabase();
                    }
                    break; // Break added here
                case "login":
                    login();
                    // Call a method to handle sign-in logic
                    break;
                case "register":
                    register();
                    break;
                case "quit":
                    System.out.println("Quitting...");
                    return;
                case "help":
                    System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "[LOGGED_OUT] for help:" + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "register " + SET_TEXT_COLOR_BLUE + "<USERNAME> <PASSWORD> <EMAIL>" + SET_TEXT_COLOR_YELLOW + " - to create an account," + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "login " + SET_TEXT_COLOR_BLUE + "<USERNAME> <PASSWORD>" + SET_TEXT_COLOR_YELLOW + " - to play chess," + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "quit - playing chess," + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "help - with possible commands." + RESET_TEXT_COLOR);
                    break;
                default:
                    System.out.println(SET_TEXT_COLOR_RED + "Unknown command. Please try again." + RESET_TEXT_COLOR);
                    break;
            }
        }
    }

    private void register() throws DataAccessException {
        ServerFacade facade = new ServerFacade(log);
        System.out.println("Please enter your username.");
        String username = scanner.nextLine();

        System.out.println("Please enter your password.");
        String password = scanner.nextLine();

        System.out.println("Please enter your email.");
        String email = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            throw new DataAccessException("Username, password, or email cannot be blank.");
        }

        RegisterRequest newchessperson = new RegisterRequest(username, password, email);
        try {
            RegisterResponse newplayer = facade.createUser(newchessperson);
            AuthData loggedinplayer = new AuthData(newplayer.authToken(), newplayer.username());
            System.out.println(SET_TEXT_ITALIC + "Registering..." + RESET_TEXT_ITALIC);
            quitButton = new EnterChessGame(log, loggedinplayer).postlogin();

        } catch (DataAccessException e) {
            System.out.println(SET_TEXT_COLOR_RED + "Username already taken." + RESET_TEXT_COLOR);
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                register();
            }
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void login() throws DataAccessException {
        ServerFacade facade = new ServerFacade(log);
        System.out.println("Please enter your username.");
        String username = scanner.nextLine();

        System.out.println("Please enter your password.");
        String password = scanner.nextLine();

        LoginRequest chessPerson = new LoginRequest(username, password);
        System.out.println(SET_TEXT_ITALIC + "Logging in..." + RESET_TEXT_ITALIC);

        try {
            LoginResponse loggedinplayer = facade.loginUser(chessPerson);
            AuthData loggedInPlayer = new AuthData(loggedinplayer.authToken(), loggedinplayer.username());
            quitButton = new EnterChessGame(log, loggedInPlayer).postlogin();

        } catch (DataAccessException | DataFormatException | IOException e) {
            System.out.println(SET_TEXT_COLOR_RED + "Incorrect Username or Password." + RESET_TEXT_COLOR);
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                login();
            }
        }
    }
}
