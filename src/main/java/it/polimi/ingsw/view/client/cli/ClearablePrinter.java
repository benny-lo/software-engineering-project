package it.polimi.ingsw.view.client.cli;

import java.io.PrintStream;

public class ClearablePrinter {
    private static final String ANSI_DELETE_LINE = "\033[A";
    private String toClear;
    private final PrintStream stream;

    public ClearablePrinter() {
        this.toClear = "";
        this.stream = System.out;
    }

    public void print(String text) {
        synchronized (stream) {
            stream.print(text);
            toClear += text.replaceAll("[^\n]", "\b").replaceAll("\n", ANSI_DELETE_LINE);
        }
    }

    public void clear() {
        synchronized (stream) {
            stream.print(toClear);
            toClear = "";
        }
    }
}
