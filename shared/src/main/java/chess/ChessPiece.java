package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
        private final ChessGame.TeamColor pieceColor;
        private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
       return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece){
            ChessPiece other = (ChessPiece) obj;
            return other.pieceColor==this.pieceColor && (other.type == this.type);
        }
        return false;
    }

    @Override
    public String toString() {
        return pieceColor + " " + type;
    }

    /**
     * Calculates all the positions a king can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition[] positions = new ChessPosition[8];
        Collection<ChessMove> moves = new HashSet<>();

        positions[0] = new ChessPosition(row + 1, col);
        positions[1] = new ChessPosition(row + 1, col + 1);
        positions[2] = new ChessPosition(row, col + 1);
        positions[3] = new ChessPosition(row - 1, col + 1);
        positions[4] = new ChessPosition(row - 1, col);
        positions[5] = new ChessPosition(row - 1, col - 1);
        positions[6] = new ChessPosition(row, col - 1);
        positions[7] = new ChessPosition(row + 1, col - 1);
        for (int i = 0; i < 8; i++){
            if (positions[i].getRow() < 0 || positions[i].getRow() > 8 || positions[i].getColumn() < 0 || positions[i].getColumn() > 8){
                positions[i] = null;
                continue;
            }
            else if(board.getPiece(positions[i]) != null && board.getPiece(positions[i]).getTeamColor() == this.pieceColor){
                positions[i] = null;
                continue;
            }
            else {
                moves.add(new ChessMove(myPosition, positions[i], null));
            }
        }
        return moves;
    }

    /**
     * Calculates all the positions a queen can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition[] positions = new ChessPosition[32];
        Collection<ChessMove> moves = new HashSet<>();
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row + i, col);
            positions[i + 8] = new ChessPosition(row - i, col);
            positions[i + 16] = new ChessPosition(row, col + i);
            positions[i + 24] = new ChessPosition(row, col - i);
        }

        for (int i = 0; i < 27; i++){
            if (positions[i].getRow() < 0 || positions[i].getRow() > 8 || positions[i].getColumn() < 0 || positions[i].getColumn() > 8){
                positions[i] = null;
                continue;
            }
            else{
                moves.add(new ChessMove(myPosition, positions[i], null));
            }
        }
        return moves;
        }

    /**
     * Calculates all the positions a bishop can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
     public Collection<ChessMove>  bishopMoves(ChessBoard board, ChessPosition myPosition) {
         int row = myPosition.getRow();
         int col = myPosition.getColumn();
         ChessPosition[] positions = new ChessPosition[32];
         Collection<ChessMove> moves = new HashSet<>();
         for (int i = 0; i < 8; i++) {
             positions[i] = new ChessPosition(row + i, col + i);
             positions[i + 8] = new ChessPosition(row - i, col - i);
             positions[i + 16] = new ChessPosition(row + i, col - i);
             positions[i + 24] = new ChessPosition(row - i, col + i);
         }

         for (int i = 0; i < 32; i++) {
             if (positions[i] == null) {
                 continue;
             }
             // Check if the position is out of bounds
             if (positions[i].getRow() < 0 || positions[i].getRow() > 8 || positions[i].getColumn() < 0 || positions[i].getColumn() > 8) {
                 positions[i] = null;
                 continue;
             } else {
                 moves.add(new ChessMove(myPosition, positions[i], null));
             }
         }
         return moves;
     }

    /**
     * Calculates all the positions a knight can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
        public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessPosition[] positions = new ChessPosition[8];
            Collection<ChessMove> moves = new HashSet<>();

            positions[0] = new ChessPosition(row + 2, col + 1);
            positions[1] = new ChessPosition(row + 2, col - 1);
            positions[2] = new ChessPosition(row - 2, col + 1);
            positions[3] = new ChessPosition(row - 2, col - 1);
            positions[4] = new ChessPosition(row + 1, col + 2);
            positions[5] = new ChessPosition(row + 1, col - 2);
            positions[6] = new ChessPosition(row - 1, col + 2);
            positions[7] = new ChessPosition(row - 1, col - 2);
            for (int i = 0; i < 8; i++){
                if (positions[i].getRow() < 0 || positions[i].getRow() > 8 || positions[i].getColumn() < 0 || positions[i].getColumn() > 8){
                    positions[i] = null;
                    continue;
                }
                else{
                    moves.add(new ChessMove(myPosition, positions[i], null));
                }
            }
            return moves;
        }

    /**
     * Calculates all the positions a rook can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
        public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            ChessPosition[] positions = new ChessPosition[27];
            Collection<ChessMove> moves = new HashSet<>();
            for (int i = 0; i < 8; i++) {
                positions[i] = new ChessPosition(row + i, col);
                positions[i + 8] = new ChessPosition(row - i, col);
                positions[i + 16] = new ChessPosition(row, col + i);
                positions[i + 24] = new ChessPosition(row, col - i);
            }

            for (int i = 0; i < 27; i++) {
                if (positions[i].getRow() < 0 || positions[i].getRow() > 8 || positions[i].getColumn() < 0 || positions[i].getColumn() > 8) {
                    positions[i] = null;
                    continue;
                } else {
                    moves.add(new ChessMove(myPosition, positions[i], null));
                }
            }
            return moves;
        }

    /**
     * Calculates all the positions a pawn can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition[] positions = new ChessPosition[3];
        Collection<ChessMove> moves = new HashSet<>();

        if (this.pieceColor == ChessGame.TeamColor.WHITE){
            positions[0] = new ChessPosition(row + 1, col);
            positions[1] = new ChessPosition(row + 1, col + 1);
            positions[2] = new ChessPosition(row + 1, col - 1);
        }
        else{
            positions[0] = new ChessPosition(row - 1, col);
            positions[1] = new ChessPosition(row - 1, col + 1);
            positions[2] = new ChessPosition(row - 1, col - 1);
        }
        for (int i = 0; i < 3; i++){
            if (positions[i].getRow() < 0 || positions[i].getRow() > 8 || positions[i].getColumn() < 0 || positions[i].getColumn() > 8){
                positions[i] = null;
                continue;
            }
            else{
                moves.add(new ChessMove(myPosition, positions[i], null));
            }
        }
        return moves;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch(type){
            case KING:
                return kingMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case ROOK:
                return rookMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
            default:
                throw new RuntimeException("Not implemented");
        }
    }
}
