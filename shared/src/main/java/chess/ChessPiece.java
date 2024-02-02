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
     * @return valid moves for the given lane
     */
    private void getValidLanes(ChessBoard board, ChessPosition[] positions) {
        for (int i = 0; i < positions.length; i++) {
            if (positions[i].getRow() < 1 || positions[i].getRow() > 8 || positions[i].getColumn() < 1 || positions[i].getColumn() > 8) {
                positions[i] = null;
                continue;
            } else if (board.getPiece(positions[i]) != null && board.getPiece(positions[i]).getTeamColor() == this.pieceColor) {
                positions[i] = null;
                for(int j = i; j< positions.length; j++){
                    positions[j] = null;
                }
                break;

            } else if (board.getPiece(positions[i]) != null && board.getPiece(positions[i]).getTeamColor() != this.pieceColor) {

                for (int j = i + 1; j < positions.length; j++) {
                    positions[j] = null;
                }
                break;
            }

        }

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
            if (positions[i].getRow() < 1 || positions[i].getRow() > 8 || positions[i].getColumn() < 1 || positions[i].getColumn() > 8){
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
        ChessPosition[] positions = new ChessPosition[8];
        Collection<ChessMove> moves = new HashSet<>();

        // check down lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row + i + 1, col);
        }
        // check down positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
        }

        // check up lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row - i - 1, col);
        }
        // check up positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
        }

        //check right lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row, col + i + 1);
        }
        // check right positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
        }

        // check left lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row, col - i - 1);
        }
        // check left positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
        }

        // check down right lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row + i + 1, col + i + 1);
        }
        // check down right positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
        }

        // check down left lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row - i - 1, col + i + 1);
        }
        // check down left positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
        }

        // check up right lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row + i + 1, col - i - 1);
        }
        // check up right positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
        }

        // check up left lane
        for (int i = 0; i < 8; i++){
            positions[i] = new ChessPosition(row - i - 1, col - i - 1);
        }
        // check up left positions
        getValidLanes(board, positions);
        for (ChessPosition position : positions) {
            if (position == null) {
                continue;
            }
            moves.add(new ChessMove(myPosition, position, null));
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
         ChessPosition[] positions = new ChessPosition[8];
         Collection<ChessMove> moves = new HashSet<>();

         // check down right lane
         for (int i = 0; i < 8; i++){
             positions[i] = new ChessPosition(row + i + 1, col + i + 1);
         }
         // check down right positions
         getValidLanes(board, positions);
         for (ChessPosition position : positions) {
             if (position == null) {
                 continue;
             }
             moves.add(new ChessMove(myPosition, position, null));
         }

         // check down left lane
         for (int i = 0; i < 8; i++){
             positions[i] = new ChessPosition(row - i - 1, col + i + 1);
         }
         // check down left positions
         getValidLanes(board, positions);
         for (ChessPosition position : positions) {
             if (position == null) {
                 continue;
             }
             moves.add(new ChessMove(myPosition, position, null));
         }

         // check up right lane
         for (int i = 0; i < 8; i++){
             positions[i] = new ChessPosition(row + i + 1, col - i - 1);
         }
         // check up right positions
         getValidLanes(board, positions);
         for (ChessPosition position : positions) {
             if (position == null) {
                 continue;
             }
             moves.add(new ChessMove(myPosition, position, null));
         }

         // check up left lane
         for (int i = 0; i < 8; i++){
             positions[i] = new ChessPosition(row - i - 1, col - i - 1);
         }
         // check up left positions
         getValidLanes(board, positions);
         for (ChessPosition position : positions) {
             if (position == null) {
                 continue;
             }
             moves.add(new ChessMove(myPosition, position, null));
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
            row -= 1;
            col -= 1;
            for (int i = 0; i < 8; i++){
                if (positions[i].getRow() < 1 || positions[i].getRow() > 8 || positions[i].getColumn() < 1 || positions[i].getColumn() > 8){
                    positions[i] = null;
                    continue;
                }
                else if (board.getPiece(positions[i]) != null && board.getPiece(positions[i]).getTeamColor() == this.pieceColor){
                    positions[i] = null;
                    continue;
                }
                else if (board.getPiece(positions[i]) != null && board.getPiece(positions[i]).getTeamColor() != this.pieceColor) {
                    moves.add(new ChessMove(myPosition, positions[i], null));
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
            ChessPosition[] positions = new ChessPosition[8];
            Collection<ChessMove> moves = new HashSet<>();
            // check down lane
            for (int i = 0; i < 8; i++){
                positions[i] = new ChessPosition(row + i + 1, col);
            }
            // check down positions
            getValidLanes(board, positions);
            for (ChessPosition position : positions) {
                if (position == null) {
                    continue;
                }
                moves.add(new ChessMove(myPosition, position, null));
            }

            // check up lane
            for (int i = 0; i < 8; i++){
                positions[i] = new ChessPosition(row - i - 1, col);
            }
            // check up positions
            getValidLanes(board, positions);
            for (ChessPosition position : positions) {
                if (position == null) {
                    continue;
                }
                moves.add(new ChessMove(myPosition, position, null));
            }

            //check right lane
            for (int i = 0; i < 8; i++){
                positions[i] = new ChessPosition(row, col + i + 1);
            }
            // check right positions
            getValidLanes(board, positions);
            for (ChessPosition position : positions) {
                if (position == null) {
                    continue;
                }
                moves.add(new ChessMove(myPosition, position, null));
            }

            // check left lane
            for (int i = 0; i < 8; i++){
                positions[i] = new ChessPosition(row, col - i - 1);
            }
            // check left positions
            getValidLanes(board, positions);
            for (ChessPosition position : positions) {
                if (position == null) {
                    continue;
                }
                moves.add(new ChessMove(myPosition, position, null));
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
    Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition[] positions = new ChessPosition[4];
        Collection<ChessMove> moves = new HashSet<>();

        // Check if white or black pawn
        //WHITE
        if(board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE){
            positions[0] = (board.getPiece(new ChessPosition(row + 1, col)) == null) ? new ChessPosition(row + 1, col) : null;
            positions[1] = (col > 1 && board.getPiece(new ChessPosition(row + 1, col-1)) != null && board.getPiece(new ChessPosition(row + 1, col-1)).pieceColor !=board.getPiece(myPosition).pieceColor) ? new ChessPosition(row + 1, col-1) : null;
            positions[2] = (col < 8 && board.getPiece(new ChessPosition(row + 1, col+1)) != null && board.getPiece(new ChessPosition(row + 1, col+1)).pieceColor !=board.getPiece(myPosition).pieceColor) ? new ChessPosition(row + 1, col+1) : null;
            positions[3] = (row == 2 && board.getPiece(new ChessPosition(row + 1, col)) == null && board.getPiece(new ChessPosition(row + 2, col)) == null) ? new ChessPosition(row + 2, col) : null;
        }
        //BLACK
        else{
            positions[0] = (board.getPiece(new ChessPosition(row - 1, col)) == null) ? new ChessPosition(row - 1, col) : null;
            positions[1] = (col > 1 && board.getPiece(new ChessPosition(row - 1, col-1)) != null && board.getPiece(new ChessPosition(row - 1, col-1)).pieceColor !=board.getPiece(myPosition).pieceColor) ? new ChessPosition(row - 1, col-1) : null;
            positions[2] = (col < 8 && board.getPiece(new ChessPosition(row - 1, col+1)) != null && board.getPiece(new ChessPosition(row - 1, col+1)).pieceColor !=board.getPiece(myPosition).pieceColor) ? new ChessPosition(row - 1, col+1) : null;
            positions[3] = (row == 7 && board.getPiece(new ChessPosition(row - 1, col)) == null && board.getPiece(new ChessPosition(row - 2, col)) == null) ? new ChessPosition(row - 2, col) : null;

        }

        // Check if BLACK or WHITE is ready to promote
        if((row == 2 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.BLACK) || (row == 7 && board.getPiece(myPosition).pieceColor == ChessGame.TeamColor.WHITE)){
            //Add each promotion move for each valid positions
            for(ChessPosition position: positions){
                if(position != null) {
                    moves.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, position, PieceType.ROOK));
                }
            }
        }
        else{
            for(ChessPosition position: positions){
                if (position != null) {
                    moves.add(new ChessMove(myPosition, position, null));
                }
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
        return switch (type) {
            case KING -> kingMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            case BISHOP -> bishopMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case PAWN -> pawnMoves(board, myPosition);
            default -> throw new RuntimeException("Not implemented");
        };
    }
}
