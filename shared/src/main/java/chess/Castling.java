package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Castling {
    private boolean BQR = true;
    private boolean BK = true;
    private boolean BKR = true;

    private boolean WQR = true;
    private boolean WK = true;
    private boolean WKR = true;

    private final ChessPosition BQRP = new ChessPosition(8,1);
    private final ChessPosition BKP = new ChessPosition(8,5);
    private final ChessPosition BKRP = new ChessPosition(8,8);

    private final ChessPosition WQRP = new ChessPosition(1,1);
    private final ChessPosition WKP = new ChessPosition(1,5);
    private final ChessPosition WKRP = new ChessPosition(1,8);

    private final ChessPiece BRPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    private final ChessPiece BKPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

    private final ChessPiece WRPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    private final ChessPiece WKPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);


    public void occupied(ChessBoard board) {

        occupiedHelper(board, BQRP, BRPiece, BQR);
        occupiedHelper(board, BKP, BKPiece, BK);
        occupiedHelper(board, BKRP, BRPiece, BKR);

        occupiedHelper(board, WQRP, WRPiece, WQR);
        occupiedHelper(board, WKP, WKPiece, WK);
        occupiedHelper(board, WKRP, WRPiece, WKR);

    };
    private void occupiedHelper(ChessBoard board, ChessPosition place, ChessPiece piece, boolean setter){
        if (board.getPiece(place) == null){
            setter = false;
        }
        else if (board.getPiece(place) != piece){
            setter = false;
        }
    }

};