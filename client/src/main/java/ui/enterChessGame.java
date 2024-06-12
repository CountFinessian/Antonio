package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.DataAccessException;
import model.*;
import responserequest.*;
import server.ServerFacade;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class enterChessGame {
    private static Scanner scanner;
    private String URL;
    private static ServerFacade facade;
    private static AuthData loogyinny;

    public enterChessGame(String URL, AuthData loggyinny) {
        this.URL = URL;
        this.loogyinny = loggyinny;
        this.scanner = new Scanner(System.in);
        this.facade = new ServerFacade(URL);
    }

    public static Boolean PostLogin() throws DataAccessException {
        boolean main_menu = true;
        while (main_menu) {
            System.out.println(STR."\{SET_TEXT_COLOR_LIGHT_GREY}Logged in as \{loogyinny.username()}.\{RESET_TEXT_COLOR}");
            System.out.println(STR."Please type a command to start playing.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "create":
                    Create();
                    break;
                case "join":
                    Join();
                    // Implementation for join
                    //Register, then Render Game
                    break;
                case "observe":
                    System.out.println("Please enter the Game ID.");
                    int gameID = Integer.parseInt(scanner.nextLine().trim().toLowerCase());
                    Display(gameID);
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

    private static void Create() throws DataAccessException {
        System.out.println("Please enter the game name.");
        String gameName = scanner.nextLine().trim().toLowerCase();

        if (gameName.isEmpty()) {
            System.out.println(STR."\{SET_TEXT_COLOR_RED}Invalid game name.\{RESET_TEXT_COLOR}");
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                Create();
            }
        }
        MakeGameRequest newGameRequest = new MakeGameRequest(gameName);

        try {
            MakeGameResponse newGame = facade.createGame(newGameRequest, loogyinny.authToken());
            int gameID = newGame.gameID();
            System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}Game ID: \{SET_TEXT_COLOR_BLUE}\{gameID}\{RESET_TEXT_COLOR}");
        } catch (DataAccessException e) {
        }
    }

    private static void Join() throws DataAccessException {

        try {
            System.out.println("Please enter the Game ID.");
            int gameID = Integer.parseInt(scanner.nextLine().trim().toLowerCase());

            System.out.println("Please enter B for Black or W for White");
            String gameColorInitial = scanner.nextLine().trim().toLowerCase();

            if (gameColorInitial.equals("b")) {
                gameColorInitial = "BLACK";
            } else if (gameColorInitial.equals("w")) {
                gameColorInitial = "WHITE";
            } else {
                throw new DataAccessException(STR."Invalid gameColorInitial: \{gameColorInitial}");
            }
            JoinGameRequest playerToJoin = new JoinGameRequest(gameColorInitial, gameID);
            facade.joinGame(playerToJoin, loogyinny.authToken());
            System.out.println(STR."\{SET_TEXT_COLOR_YELLOW}\{loogyinny.username()} \{SET_TEXT_COLOR_BLUE}joined the game!");
            Display(gameID);
        } catch (DataAccessException e) {
            System.out.println(STR."\{SET_TEXT_COLOR_RED}Invalid game ID or Game Color or GameFull\{RESET_TEXT_COLOR}");
            System.out.println("Press any key to try again.");
            System.out.println("Or type exit to go back to main menu");

            String todo = scanner.nextLine().trim().toLowerCase();
            if (!todo.equals("exit")) {
                Join();
            }
        }
    }

    private static void Display(int gameID) throws DataAccessException {
        GameData theGame;
        GetAllGamesResponse games = facade.listGames(loogyinny.authToken());
        for (GameData game : games.games()) {
            if (game.gameID() == gameID) {
                theGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                theGame.game().boardRefill();
                displayBoard(theGame.game().getBoard());
                return;
            }
        }
        System.out.println(STR."\{SET_TEXT_COLOR_RED}Invalid game ID \{RESET_TEXT_COLOR}");
        System.out.println("Try a valid ID.");
        System.out.println("Or type exit to go back to main menu");

        String todo = scanner.nextLine().trim().toLowerCase();
        if (!todo.equals("exit")) {
            Display(gameID);
        }
    }

    private static void displayBoard(ChessBoard board) {
        // Print the top border (column labels)
        System.out.print("  ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print(col);
            System.out.print("---");
        }
        System.out.println();

        for (int row = 8; row >= 1; row--) {
            System.out.print(STR."\{row} ");  // Print row number on the left

            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String square;
                if (piece == null) {
                    // Checkered pattern for board background
                    if ((row + col) % 2 == 0) {
                        square = SET_BG_COLOR_LIGHT_GREY + EMPTY + RESET_ALL;
                    } else {
                        square = SET_BG_COLOR_BLACK + EMPTY + RESET_ALL;
                    }
                } else {
                    String pieceString;
                    switch (piece.getPieceType()) {
                        case PAWN:
                            pieceString = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN;
                            break;
                        case ROOK:
                            pieceString = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK;
                            break;
                        case KNIGHT:
                            pieceString = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT;
                            break;
                        case BISHOP:
                            pieceString = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP;
                            break;
                        case QUEEN:
                            pieceString = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN;
                            break;
                        case KING:
                            pieceString = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING;
                            break;
                        default:
                            pieceString = EMPTY;
                    }
                    if ((row + col) % 2 == 0) {
                        square = SET_BG_COLOR_LIGHT_GREY + pieceString + RESET_ALL;
                    } else {
                        square = SET_BG_COLOR_BLACK + pieceString + RESET_ALL;
                    }
                }
                System.out.print(square);
            }

            System.out.print(STR." \{row}");  // Print row number on the right
            System.out.println();
        }

        // Print the bottom border (column labels)
        System.out.print("  ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print(STR."\{col}---");
        }
        System.out.println();
    }

}


