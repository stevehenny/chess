
package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

import static java.awt.Color.GREEN;
import static ui.EscapeSequences.*;


public class Repl {
    private final ChessClient client;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 2;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";
    private static Random rand = new Random();

    public Repl(String serverUrl) {
        this.client = new ChessClient(serverUrl);
    }


    public void run() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        String[][] board = new String[8][8];
        String afterSpacing = "\u2001\u2005";
        String beforeSpacing = "\u2006";
        board[0] = new String[]{beforeSpacing+"R"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"Q"+afterSpacing, beforeSpacing+"K"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"R"+afterSpacing};
        board[1] = new String[]{beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing};
        for (int i = 2; i < 6; i++) {
            board[i] = new String[]{beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing, beforeSpacing+" "+afterSpacing};
        }
        board[6] = new String[]{beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing, beforeSpacing+"P"+afterSpacing};
        board[7] = new String[]{beforeSpacing+"R"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"Q"+afterSpacing, beforeSpacing+"K"+afterSpacing, beforeSpacing+"B"+afterSpacing, beforeSpacing+"N"+afterSpacing, beforeSpacing+"R"+afterSpacing};
        printWhitePerspective(out, board);


        System.out.println("Welcome to Chess!");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }

    public void printTopRow(PrintStream out) {

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
        printTopRow(out);
        out.print(moveCursorToLocation(0,0));
        String spacing = "\u2001\u2005\u2006";
        String background = SET_BG_COLOR_LIGHT_GREY;
        String[] rowLabels = {" 1"+spacing, " 2"+spacing, " 3"+spacing, " 4"+spacing, " 5"+spacing, " 6"+spacing, " 7"+spacing, " 8"+spacing};
//        System.out.print(SET_BG_COLOR_BLACK + EMPTY);
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
    printTopRow(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void setGreySquare(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

//    public print

    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length > 0) {
            serverUrl = args[0];
        }
        var repl = new Repl(serverUrl);
        repl.run();
    }

}