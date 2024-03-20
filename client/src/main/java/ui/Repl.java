package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to chess. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result + "\n");
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.println("RESET>>> ");
    }

    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length > 0) {
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
    }

}

