package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class PrintBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 2;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";
    public PrintStream out;

    public PrintBoard() {
        this.out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }


    public void printBoards() {
        String[][] board = new String[8][8];
        String afterSpacing = "\u2001\u2005";
        String beforeSpacing = "\u2006";
        board[0] = new String[]{beforeSpacing + "R" + afterSpacing, beforeSpacing + "N" + afterSpacing, beforeSpacing + "B" + afterSpacing, beforeSpacing + "Q" + afterSpacing, beforeSpacing + "K" + afterSpacing, beforeSpacing + "B" + afterSpacing, beforeSpacing + "N" + afterSpacing, beforeSpacing + "R" + afterSpacing};
        board[1] = new String[]{beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing};
        for (int i = 2; i < 6; i++) {
            board[i] = new String[]{beforeSpacing + " " + afterSpacing, beforeSpacing + " " + afterSpacing, beforeSpacing + " " + afterSpacing, beforeSpacing + " " + afterSpacing, beforeSpacing + " " + afterSpacing, beforeSpacing + " " + afterSpacing, beforeSpacing + " " + afterSpacing, beforeSpacing + " " + afterSpacing};
        }
        board[6] = new String[]{beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing, beforeSpacing + "P" + afterSpacing};
        board[7] = new String[]{beforeSpacing + "R" + afterSpacing, beforeSpacing + "N" + afterSpacing, beforeSpacing + "B" + afterSpacing, beforeSpacing + "Q" + afterSpacing, beforeSpacing + "K" + afterSpacing, beforeSpacing + "B" + afterSpacing, beforeSpacing + "N" + afterSpacing, beforeSpacing + "R" + afterSpacing};
        printWhitePerspective(out, board);
        printBlackPerspective(out, board);
    }

    public void printTopRowWhite(PrintStream out) {

        out.print("   ");
        setGreySquare(out);
        String spacing = "\u2001\u2005";
        System.out.print(SET_BG_COLOR_LIGHT_GREY + EMPTY  + spacing + " ");
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            out.print(" " + (char) ('a' + i) + spacing);
        }
        out.print( EMPTY + ' ');
        out.print(SET_BG_COLOR_BLACK);
        out.print("\n");
    }

    public void printWhitePerspective(PrintStream out, String[][] board) {
        printTopRowWhite(out);
//        out.print(moveCursorToLocation(0,0));
        String spacing = "\u2001\u2005\u2006";
        String background = SET_BG_COLOR_LIGHT_GREY;
        String[] rowLabels = {" 1"+spacing, " 2"+spacing, " 3"+spacing, " 4"+spacing, " 5"+spacing, " 6"+spacing, " 7"+spacing, " 8"+spacing};
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print(SET_BG_COLOR_LIGHT_GREY + rowLabels[i]);
            for (int j = 0; j < BOARD_SIZE_IN_SQUARES; j++) {
                if ((i + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(SET_TEXT_COLOR_BLUE);
                if (i >=6){
                    out.print(SET_TEXT_COLOR_RED);
                }
                out.print(board[i][j]);
            }
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(SET_BG_COLOR_LIGHT_GREY + rowLabels[i]);
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print("\n");
        }
        printTopRowWhite(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void printBlackPerspective(PrintStream out, String[][] board) {
        printTopRowBlack(out);
        String spacing = "\u2001\u2005\u2006";
        String background = SET_BG_COLOR_LIGHT_GREY;
        String[] rowLabels = {" 1"+spacing, " 2"+spacing, " 3"+spacing, " 4"+spacing, " 5"+spacing, " 6"+spacing, " 7"+spacing, " 8"+spacing};
        for (int i = BOARD_SIZE_IN_SQUARES - 1; i >= 0; i--) {
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print(SET_BG_COLOR_LIGHT_GREY + rowLabels[i]);
            for (int j = BOARD_SIZE_IN_SQUARES - 1; j >= 0; j--) {
                if ((i + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                out.print(SET_TEXT_COLOR_BLUE);
                if (i >=6){
                    out.print(SET_TEXT_COLOR_RED);
                }
                out.print(board[i][j]);
            }
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(SET_BG_COLOR_LIGHT_GREY + rowLabels[i]);
            out.print(SET_BG_COLOR_BLACK + EMPTY);
            out.print("\n");
        }
        printTopRowBlack(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private void printTopRowBlack(PrintStream out) {
        out.print("   ");
        setGreySquare(out);
        String spacing = "\u2001\u2005";
        System.out.print(SET_BG_COLOR_LIGHT_GREY + EMPTY  + spacing + " ");
        for (int i = BOARD_SIZE_IN_SQUARES - 1; i >= 0; i--) {
            out.print(" " + (char) ('a' + i) + spacing);
        }
        out.print( EMPTY + ' ');
        out.print(SET_BG_COLOR_BLACK);
        out.print("\n");
    }

    public void setGreySquare(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
