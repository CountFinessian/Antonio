import chess.ChessGame;
import chess.ChessPiece;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        Server myChessServer = new Server();
        myChessServer.run(8080);

        System.out.println("♕ 240 Chess Server: " + piece);
    }
}