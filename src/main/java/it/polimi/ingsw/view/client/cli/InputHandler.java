package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.view.client.InputReceiver;

import java.util.*;

public class InputHandler implements Runnable {
    private final InputReceiver inputReceiver;
    private final Scanner in;

    /**
     * Constructor for the class
     * @param inputReceiver - the inputReceiver of the input handler
     */
    public InputHandler(InputReceiver inputReceiver) {
        this.inputReceiver = inputReceiver;
        this.in = new Scanner(System.in);
    }

    /**
     * Override of the run method, handling all the cases : help, login, create game, select game, handle the living room
     * handle the bookshelf, enter the chat, exit the chat, direct message.
     */
    @Override
    public void run() {
        while(true) {
            String line;
            try {
                line = in.nextLine();
            } catch (NoSuchElementException e) {
                continue;
            } catch (IllegalStateException e) {
                return;
            }
            if (line.length() == 0) continue;
            if (line.charAt(0) != '/') {
                inputReceiver.writeChat(new ChatMessage(line.replace("\n", "").replace("\r", ""), "all"));
                continue;
            }

            StringTokenizer tokenizer = new StringTokenizer(line, " ,");

            if (!tokenizer.hasMoreTokens()) continue;

            String command = tokenizer.nextToken();
            try {
                switch (command) {
                    case "/help" -> {
                        if (tokenizer.countTokens() == 0) {
                            CLInterfacePrinter.printHelp();
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/login" -> {
                        if (tokenizer.countTokens() == 1) {
                            String nickname = tokenizer.nextToken();
                            inputReceiver.login(new Nickname(nickname));
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/create_game" -> {
                        if (tokenizer.countTokens() == 2) {
                            int numberPlayer = Integer.parseInt(tokenizer.nextToken());
                            int numberCommonGoalCards = Integer.parseInt(tokenizer.nextToken());
                            inputReceiver.createGame(new GameInitialization(numberPlayer, numberCommonGoalCards));
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/select_game" -> {
                        if (tokenizer.countTokens() == 1) {
                            int id = Integer.parseInt(tokenizer.nextToken());
                            inputReceiver.selectGame(new GameSelection(id));
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/living_room" -> {
                        if (tokenizer.countTokens() % 2 == 0) {
                            List<Position> positions = new ArrayList<>();
                            while (tokenizer.hasMoreTokens()) {
                                int x = Integer.parseInt(tokenizer.nextToken());
                                int y = Integer.parseInt(tokenizer.nextToken());
                                positions.add(new Position(x, y));
                            }
                            inputReceiver.selectFromLivingRoom(new LivingRoomSelection(positions));
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/bookshelf" -> {
                        if (tokenizer.countTokens() > 1) {
                            int column = Integer.parseInt(tokenizer.nextToken());

                            List<Integer> permutation = new ArrayList<>();
                            while (tokenizer.hasMoreTokens()) {
                                permutation.add(Integer.parseInt(tokenizer.nextToken()));
                            }
                            inputReceiver.insertInBookshelf(new BookshelfInsertion(column, permutation));
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/enter_chat" -> {
                        if (tokenizer.countTokens() == 0) {
                            inputReceiver.enterChat();
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/exit_chat" -> {
                        if (tokenizer.countTokens() == 0) {
                            inputReceiver.exitChat();
                        } else {
                            CLInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/dm" -> {
                        if (tokenizer.countTokens() == 2) {
                            String receiver = tokenizer.nextToken();
                            String text = tokenizer.nextToken();
                            inputReceiver.writeChat(new ChatMessage(text, receiver));
                        } else
                            CLInterfacePrinter.printIncorrectCommand();
                    }
                    case "/exit" -> {
                        if (tokenizer.countTokens() == 0)
                            inputReceiver.exit();
                        else
                            CLInterfacePrinter.printIncorrectCommand();
                    }
                    default -> CLInterfacePrinter.printWrongCommand();
                }
            } catch (NumberFormatException e) {
                CLInterfacePrinter.printIncorrectCommand();
            }
        }
    }
}
