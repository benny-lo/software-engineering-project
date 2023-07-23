package it.polimi.ingsw.view.client.cli;

import java.io.PrintStream;

public class ClearablePrinter {
    private static final String ANSI_DELETE_LINE = "\033[A";
    private static String toClear = "";
    private static final PrintStream stream = System.out;

    public static void printAndClearNext(String text) {
        synchronized (stream) {
            stream.print(text);
            record(text);
        }
    }

    public static void printAndNoClearNext(String text) {
        synchronized (stream) {
            stream.print(text);
        }
    }

    public static void record(String text) {
        synchronized (stream) {
            StringBuilder builder = new StringBuilder(text);
            builder.reverse();
            toClear = builder.toString().replaceAll("[^\n]", " ").replaceAll("\n", ANSI_DELETE_LINE + "\r") + toClear;
        }
    }

    public static void clear() {
        synchronized (stream) {
            stream.print(toClear + "\r");
            toClear = "";
        }
    }
}
