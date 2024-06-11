package ui;

import exception.DataAccessException;
import responserequest.*;
import server.ServerFacade;
import java.util.Arrays;
import java.util.Scanner;

public class consoleLogger {
    private final String Log;

    public consoleLogger(String serverUrl) {
        this.Log = serverUrl;
    }

        public void run() throws DataAccessException {
            ServerFacade facade = new ServerFacade(Log);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println(STR."\{EscapeSequences.SET_TEXT_BLINKING}Hello, please enter a commnad.\{EscapeSequences.RESET_TEXT_COLOR}");
                String input = scanner.nextLine().trim().toLowerCase();

                switch (input) {
                    case "login":
                        System.out.println("Signing in...");
                        Login();
                        // Call a method to handle sign-in logic
                        break;
                    case "register":
                        System.out.println("Registering...");
                    case "quit":
                        System.out.println("Quitting...");
                        return;
                    case "help":
                        System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY}[LOGGED_OUT] for help:\{EscapeSequences.RESET_TEXT_COLOR}");
                        System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}register \{EscapeSequences.SET_TEXT_COLOR_BLUE}<USERNAME> <PASSWORD> <EMAIL>\{EscapeSequences.SET_TEXT_COLOR_YELLOW} - to create an account,\{EscapeSequences.RESET_TEXT_COLOR}");
                        System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}login \{EscapeSequences.SET_TEXT_COLOR_BLUE}<USERNAME> <PASSWORD>\{EscapeSequences.SET_TEXT_COLOR_YELLOW} - to play chess,\{EscapeSequences.RESET_TEXT_COLOR}");
                        System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}quit - playing chess,\{EscapeSequences.RESET_TEXT_COLOR}");
                        System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}help - with possible commands.\{EscapeSequences.RESET_TEXT_COLOR}");
                        break;
                    default:
                        System.out.println(STR."\{EscapeSequences.SET_TEXT_COLOR_YELLOW}Unknown command. Please try again.\{EscapeSequences.RESET_TEXT_COLOR}");

                        break;
                }
            }
        }

        private void signIn() throws DataAccessException {
            System.out.print("Enter Username: ");
        }

        private void Login() throws DataAccessException {
            System.out.print("Enter Username: ");
        }

//        RegisterRequest new_chessPerson = new RegisterRequest("KAWILL", "D3cember!", "kman16409@gmail.com");
//        LoginRequest chessPerson = new LoginRequest("KAWILL", "D3cember!");
//        MakeGameRequest gameBoard = new MakeGameRequest("Frozone");
//        JoinGameRequest joinGame = new JoinGameRequest("WHITE", 3);
//        try {
////            RegisterResponse newplayer = facade.createUser(new_chessPerson);
//            LoginResponse player = facade.loginUser(chessPerson);
//            MakeGameResponse gameOn = facade.createGame(gameBoard, player.authToken());
//            facade.joinGame(joinGame, player.authToken());
//            System.out.print(facade.listGames(player.authToken()));
////            facade.deleteDatabase();
//
////            facade.deleteUser(player.authToken());
//            // Handle the player response if necessary
//        } catch (DataAccessException player) {
//            System.out.print(player.getMessage());
//        }
    }

