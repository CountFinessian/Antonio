package ui;

import chess.*;
import exception.DataAccessException;
import model.AuthData;
import model.GameData;
import responserequest.GetAllGamesResponse;
import server.ServerFacade;
import ui.websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import static ui.EscapeSequences.*;

public class GamePlay {
    private static Scanner scanner;
    private String url;
    private static ServerFacade facade;
    private static AuthData loogyinny;
    int gameID;
    boolean observer;
    WebSocketFacade websocketfacade;
    public GameData theGame;

    public GamePlay (String url, AuthData loggyinny, int gameID, boolean observer) throws DataFormatException, DataAccessException {
        this.url = url;
        this.loogyinny = loggyinny;
        this.scanner = new Scanner(System.in);
        this.facade = new ServerFacade(url);
        this.gameID = gameID;
        this.observer = observer;

        this.websocketfacade = new WebSocketFacade(url, this);

    }
    public void display() throws DataAccessException, IOException, InvalidMoveException {
        GetAllGamesResponse games = facade.listGames(loogyinny.authToken());
        for (GameData game : games.games()) {
            if (game.gameID() == gameID) {
                theGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                theGame.game().boardRefill();
                ConnectToWebSocketGame();
                StartGamePlay();
                return;
            }
        }
        System.out.println(SET_TEXT_COLOR_RED + "Invalid game ID " + RESET_TEXT_COLOR);
    }

    public void displayboard(ChessBoard board, Collection<ChessMove> highlightPositions) {
        // Print the top border (column labels)
        System.out.print("  ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print(col + "---");
        }
        System.out.println();

        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " ");  // Print row number on the left

            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(currentPos);
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

                // Highlight positions if they are in the set
                if (highlightPositions != null && highlightPositions.contains(currentPos)) {
                    square = SET_BG_COLOR_GREEN + square + RESET_ALL;
                }

                System.out.print(square);
            }

            System.out.print(" " + row);  // Print row number on the right
            System.out.println();
        }

        // Print the bottom border (column labels)
        System.out.print("  ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print(col + "---");
        }
        System.out.println();
    }

    public void ConnectToWebSocketGame() throws IOException {
        websocketfacade.CONNNECT(loogyinny.username(), theGame);
    }

    public void StartGamePlay() throws IOException, InvalidMoveException {
        Boolean stay = true;
        while (stay) {
            System.out.println("You are in the game, please type a command.");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "redraw":
                    displayboard(theGame.game().getBoard(), null);
                    break;
                case "leave":
                    websocketfacade.LEAVE(loogyinny.username(), theGame);
                    stay = false;
                    break;
                case "move":
                    if (theGame.blackUsername() != null && theGame.whiteUsername() != null) {
                        //white could move for black.
                        //getGameData().game().getBoard()
                        System.out.println("Please enter the coordinates of the piece you'd like to move in coordinate notion.");
                        ChessPosition starting = parsePosition(scanner.nextLine().trim().toLowerCase());
                        System.out.println("Please enter the coordinates of where'd you like to go with it.");
                        ChessPosition ending = parsePosition(scanner.nextLine().trim().toLowerCase());
                        System.out.println("Please enter a promotion piece");
                        ChessPiece.PieceType pp = parsePromotionPiece(scanner.nextLine().trim().toLowerCase());

                        if (theGame.game().validMoves(starting).contains(ending)){

                            ChessMove move = new ChessMove(starting, ending, pp);
                            try {
                                theGame.game().makeMove(move);
                                websocketfacade.MAKE_MOVE(move, theGame, loogyinny.username());

                            } catch (InvalidMoveException e) {
                                System.out.println("invalid move.");
                            }
                        }
                    }
                    System.out.println("There are not enough people in the game.");

                case "highlight":
                    System.out.println("Please enter the coordinates of the piece you'd like to highlight in coordinate notation.");
                    ChessPosition highlightPosition = parsePosition(scanner.nextLine().trim().toLowerCase());
                    Collection<ChessMove> validMoves = theGame.game().validMoves(highlightPosition);
                    if (validMoves == null || validMoves.isEmpty()) {
                        System.out.println("No valid moves for the selected piece.");
                    } else {
                        displayboard(theGame.game().getBoard(), validMoves);
                    }
                    break;


                case "resign":
                    return;
                case "help":
                    System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "[IN_GAME] for help:" + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "help " + SET_TEXT_COLOR_BLUE + "Displays text informing the user what actions they can take." + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "redraw " + SET_TEXT_COLOR_BLUE + "Redraws the chess board upon the user’s request." + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "leave - Removes the user from the game," + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "move - Allow the user to input what move they want to make." + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "highlight - Allows the user to input the piece for which they want to highlight legal moves." + RESET_TEXT_COLOR);
                    System.out.println(SET_TEXT_COLOR_YELLOW + "resign - the user forfeits the game and the game is over." + RESET_TEXT_COLOR);
                    break;
                default:
                    System.out.println(SET_TEXT_COLOR_RED + "Unknown command. Please try again." + RESET_TEXT_COLOR);
                    break;
            }
        }
    }

    private ChessPosition parsePosition(String input) {
        input = input.replaceAll("[(),]", "").trim(); // Remove parentheses and commas
        char col = input.charAt(0);
        int row = Character.getNumericValue(input.charAt(1));
        return new ChessPosition(row, col - 'a' + 1);
    }

    private ChessPiece.PieceType parsePromotionPiece(String input) {
        switch (input) {
            case "q": return ChessPiece.PieceType.QUEEN;
            case "r": return ChessPiece.PieceType.ROOK;
            case "b": return ChessPiece.PieceType.BISHOP;
            case "n": return ChessPiece.PieceType.KNIGHT;
            default: return null; // No promotion
        }
    }


}
