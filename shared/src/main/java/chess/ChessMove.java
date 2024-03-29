package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition finalPosition;
    private final ChessPosition startPosition;
    private final ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.finalPosition = endPosition;
        this.startPosition = startPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return finalPosition;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessMove){
            ChessMove other = (ChessMove) obj;
            return this.finalPosition.equals(other.finalPosition) && this.startPosition.equals(other.startPosition) && this.promotionPiece == other.promotionPiece;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "startPosition" + startPosition +
                ", finalPosition=" + finalPosition +
                ", promotionPiece=" + promotionPiece +
                '}';
    }

    @Override
    public int hashCode() {
        // choose a prime number
        int result = 11;

        result += 17 * result + startPosition.hashCode();
        result += 17 * result + finalPosition.hashCode();
        result += (promotionPiece != null) ? 17 * result + promotionPiece.hashCode() : 0;
        return result;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }
}
