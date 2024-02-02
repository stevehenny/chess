package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;
    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }

        if (board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING) {
            Collection<ChessMove> moves =  board.getPiece(startPosition).pieceMoves(board, startPosition);
            if(moves == null){
                return null;
            }
            moves.removeIf(this::moveWillCauseCheck);
            return moves;
        } else {
            Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
            moves.removeIf(this::moveWillCauseCheck);
            return moves;
        }
    }
    public boolean moveWillCauseCheck(ChessMove move) {
        ChessGame newGame = new ChessGame();
        newGame.setBoard(board);
        newGame.setTeamTurn(teamTurn);
        try {
            newGame.makeMove(move);
        } catch (InvalidMoveException e) {
            return true;
        }
        return newGame.isInCheck(teamTurn);
    }

    private Collection<ChessMove> moveToGetOutOfCheck(ChessPosition startPosition) {
        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        ChessPosition kingPosition = findKing(teamTurn);
        ChessGame newGame = new ChessGame();
        newGame.setBoard(board);
        newGame.setTeamTurn(teamTurn);
        for(ChessMove move : moves){
            ChessPiece piece = board.getPiece(startPosition);
            newGame.board.addPiece(startPosition, null);
            newGame.board.addPiece(move.getEndPosition(), piece);
            if(!newGame.isInCheck(teamTurn)){
                moves.remove(move);
            }
            newGame.setBoard(board);
            newGame.setTeamTurn(teamTurn);
        }
        return moves;
    }

    private boolean pieceIsPinned(ChessPosition startPosition) {
        ChessGame newGame = new ChessGame();
        newGame.setBoard(board);
        newGame.setTeamTurn(teamTurn);
        ChessPosition kingPosition = newGame.findKing(teamTurn);
        if (!isInCheck(teamTurn)){
            newGame.board.addPiece(startPosition, null);
            return newGame.isInCheck(teamTurn);

        }
        return false;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (move == null) {
            throw new InvalidMoveException("Move is null");
        }
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not this team's turn");
        }
        if (board.getPiece(move.getStartPosition()).pieceMoves(board, move.getStartPosition()).contains(move)) {
            if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN && (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8)){
                board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
            }
            else{
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            }
            board.addPiece(move.getStartPosition(), null);
            if (teamTurn == TeamColor.WHITE) {
                teamTurn = TeamColor.BLACK;
            } else {
                teamTurn = TeamColor.WHITE;
            }
        } else {
            throw new InvalidMoveException("Invalid move");
        }
    }

    public ChessPosition findKing(TeamColor teamColor){
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                if(board.getPiece(new ChessPosition(i+1, j+1)) != null){
                    if(board.getPiece(new ChessPosition(i+1, j+1)).getTeamColor() == teamColor && board.getPiece(new ChessPosition(i+1, j+1)).getPieceType() == ChessPiece.PieceType.KING){
                        return new ChessPosition(i+1, j+1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */


    public boolean isInCheck(TeamColor teamColor) {

        if (teamColor == TeamColor.WHITE) {
            ChessPosition kingPosition = findKing(TeamColor.WHITE);
            if(kingPosition == null){
                return false;
            }
            return isUnderAttack(kingPosition, TeamColor.BLACK);
        } else {
            ChessPosition kingPosition = findKing(TeamColor.BLACK);
            if(kingPosition == null){
                return false;
            }
            return isUnderAttack(kingPosition, TeamColor.WHITE);
        }
    }

    private boolean isUnderAttack(ChessPosition kingPosition, TeamColor teamColor) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition position = new ChessPosition(i + 1, j + 1);
                if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = board.getPiece(position).pieceMoves(board, position);
                    if(moves == null){
                        continue;
                    }
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE){
            ChessPosition kingPosition = findKing(TeamColor.WHITE);
            if(kingPosition == null){
                return false;
            }
            Collection<ChessMove> moves = board.getPiece(kingPosition).pieceMoves(board, kingPosition);
            return isInCheck(TeamColor.WHITE) && kingValidMoves(kingPosition, moves).isEmpty();
        }
        else{
            ChessPosition kingPosition = findKing(TeamColor.BLACK);
            if(kingPosition == null){
                return false;
            }
            Collection<ChessMove> moves = board.getPiece(kingPosition).pieceMoves(board, kingPosition);
            return isInCheck(TeamColor.BLACK) && kingValidMoves(kingPosition, moves).isEmpty();
        }

    }

    public Collection<ChessMove> kingValidMoves(ChessPosition startPosition, Collection<ChessMove> moves){
//        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        moves.removeIf(move -> isUnderAttack(move.getEndPosition(), board.getPiece(startPosition).getTeamColor()));
        return moves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE){
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    ChessPosition position = new ChessPosition(i+1, j+1);
                    if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() == TeamColor.WHITE){
                        Collection<ChessMove> moves = board.getPiece(position).pieceMoves(board, position);
                        if(moves != null && !moves.isEmpty()){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        else{
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    ChessPosition position = new ChessPosition(i+1, j+1);
                    if(board.getPiece(position) != null && board.getPiece(position).getTeamColor() == TeamColor.BLACK){
                        Collection<ChessMove> moves = board.getPiece(position).pieceMoves(board, position);
                        if(moves != null && !moves.isEmpty()){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessGame other = (ChessGame) obj;
        return board.equals(other.board) && teamTurn == other.teamTurn;
    }

    @Override
    public int hashCode() {
        int result = 11;
        result += 17 * result + board.hashCode();
        result += 17 * result + teamTurn.hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }
}
