package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class InputHandler implements Runnable {
    private final InputReceiver inputReceiver;
    private final Scanner in;

    public InputHandler(InputReceiver inputReceiver) {
        this.inputReceiver = inputReceiver;
        this.in = new Scanner(System.in);
    }

    @Override
    public void run() {
        while(true) {
            String line = in.nextLine();

            if (line.charAt(0) != '/') {
                inputReceiver.message(line.replace("\n", "").replace("\r", ""));
                continue;
            }

            StringTokenizer tokenizer = new StringTokenizer(line, " ,");

            if (!tokenizer.hasMoreTokens()) continue;

            String command = tokenizer.nextToken();
            if (command.equals("/login")) {
                if (tokenizer.countTokens() == 1) {
                    String nickname = tokenizer.nextToken();
                    inputReceiver.login(nickname);
                }
            } else if (command.equals("/create_game")) {
                if (tokenizer.countTokens() == 2) {
                    int numberPlayer = Integer.parseInt(tokenizer.nextToken());
                    int numberCommonGoalCards = Integer.parseInt(tokenizer.nextToken());
                    inputReceiver.createGame(numberPlayer, numberCommonGoalCards);
                }
            } else if (command.equals("/join_game")) {
                if (tokenizer.countTokens() == 1) {
                    int id = Integer.parseInt(tokenizer.nextToken());
                    inputReceiver.joinGame(id);
                }
            } else if (command.equals("/living_room")) {
                if (tokenizer.countTokens() % 2 == 0) {
                    List<Position> positions = new ArrayList<>();
                    while(tokenizer.hasMoreTokens()) {
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        positions.add(new Position(x, y));
                    }
                    inputReceiver.livingRoom(positions);
                }
            } else if (command.equals("/bookshelf")) {
                if (tokenizer.countTokens() > 1) {
                    int column = Integer.parseInt(tokenizer.nextToken());

                    List<Integer> permutation = new ArrayList<>();
                    while(tokenizer.hasMoreTokens()) {
                        permutation.add(Integer.parseInt(tokenizer.nextToken()));
                    }
                    inputReceiver.bookshelf(column, permutation);
                }
            } else if (command.equals("/enter_chat")) {
                if (tokenizer.countTokens() == 0) {
                    inputReceiver.enterChat();
                }
            } else if (command.equals("/exit_chat")) {
                if (tokenizer.countTokens() == 0) {
                    inputReceiver.exitChat();
                }
            }
        }
    }
}
