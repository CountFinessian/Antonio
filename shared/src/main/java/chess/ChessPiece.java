package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;



/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece piece = board.getPiece(myPosition);

        return switch (piece.getPieceType()) {
        case KING -> new KING().calculateMoves(board, myPosition);
        case QUEEN -> new QUEEN().calculateMoves(board, myPosition);
        case BISHOP -> new BISHOP().calculateMoves(board, myPosition);
        case KNIGHT -> new KNIGHT().calculateMoves(board, myPosition);
        case ROOK -> new ROOK().calculateMoves(board, myPosition);
        case PAWN -> new PAWN().calculateMoves(board, myPosition);
        };


        //switch statements on whatever your piece move calculator returns.
    }

    @Override
    public String toString() {
            return switch (type) {
                case KING -> pieceColor == ChessGame.TeamColor.WHITE ? "K" : "k";
                case QUEEN -> pieceColor == ChessGame.TeamColor.WHITE ? "Q" : "q";
                case BISHOP -> pieceColor == ChessGame.TeamColor.WHITE ? "B" : "b";
                case KNIGHT -> pieceColor == ChessGame.TeamColor.WHITE ? "N" : "n";
                case ROOK -> pieceColor == ChessGame.TeamColor.WHITE ? "R" : "r";
                case PAWN -> pieceColor == ChessGame.TeamColor.WHITE ? "P" : "p";
            };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
}
