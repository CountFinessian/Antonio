import chess.*;
import server.ServerFacade;

public class Main {
    public static void main(String[] args) {

        Server myChessServer = new Server();
        myChessServer.run(8080);

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}