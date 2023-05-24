package it.polimi.ingsw.view.client.cli;

import it.polimi.ingsw.model.Position;
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
                            TextInterfacePrinter.printHelp();
                            inputReceiver.getStatus();
                        } else {
                            TextInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/login" -> {
                        if (tokenizer.countTokens() == 1) {
                            String nickname = tokenizer.nextToken();
                            inputReceiver.login(new Nickname(nickname));
                        } else {
                            TextInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/create_game" -> {
                        if (tokenizer.countTokens() == 2) {
                            int numberPlayer = Integer.parseInt(tokenizer.nextToken());
                            int numberCommonGoalCards = Integer.parseInt(tokenizer.nextToken());
                            inputReceiver.createGame(new GameInitialization(numberPlayer, numberCommonGoalCards));
                        } else {
                            TextInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/select_game" -> {
                        if (tokenizer.countTokens() == 1) {
                            int id = Integer.parseInt(tokenizer.nextToken());
                            inputReceiver.selectGame(new GameSelection(id));
                        } else {
                            TextInterfacePrinter.printIncorrectCommand();
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
                            TextInterfacePrinter.printIncorrectCommand();
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
                            TextInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/enter_chat" -> {
                        if (tokenizer.countTokens() == 0) {
                            inputReceiver.enterChat();
                        } else {
                            TextInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/exit_chat" -> {
                        if (tokenizer.countTokens() == 0) {
                            inputReceiver.exitChat();
                        } else {
                            TextInterfacePrinter.printIncorrectCommand();
                        }
                    }
                    case "/dm" -> {
                        if (tokenizer.countTokens() != 2) return;
                        String receiver = tokenizer.nextToken();
                        String text = tokenizer.nextToken();
                        inputReceiver.writeChat(new ChatMessage(text, receiver));
                    }
                    default -> TextInterfacePrinter.printWrongCommand();
                }
            } catch (NumberFormatException e) {
                TextInterfacePrinter.printIncorrectCommand();
            }
        }
    }
}
