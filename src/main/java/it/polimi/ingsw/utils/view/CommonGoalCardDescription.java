package it.polimi.ingsw.utils.view;

public class CommonGoalCardDescription {
    public static String getDescription(int id) {
        switch (id) {
            case 0 -> {
                return "\nTwo groups each containing 4 tiles of the same type in a 2x2 square.\nThe tiles of one square can be different\nfrom those of the other square.\n";
            }
            case 1 -> {
                return "\nTwo columns each formed by 6 different types of tiles.\n";
            }
            case 2 -> {
                return "\nFour groups each containing at least 4 tiles of the same type.\nThe tiles of one group can be different\nfrom those of another group.\n";
            }
            case 3 -> {
                return "\nSix groups each containing at least 2 tiles of the same type.\nThe tiles of one group can be different\nfrom those of another group.\n";
            }
            case 4 -> {
                return "\nThree columns each formed by 6 tiles of maximum three different types.\nOne column can show the same or a different\ncombination of another column\n";
            }
            case 5 -> {
                return "\nTwo lines each formed by 5 different types of tiles.\nOne line can show the same or a different\ncombination of the other line.\n";
            }
            case 6 -> {
                return "\nFour lines each formed by 5 tiles of maximum three different types.\nOne line can show the same or a different\ncombination of another line.\n";
            }
            case 7 -> {
                return "\nFour tiles of the same type in the four corners of the bookshelf.\n";
            }
            case 8 -> {
                return "\nEight or more tiles of the same type with no restrictions\nabout the position of these tiles.\n";
            }
            case 9 -> {
                return "\nFive tiles of the same type forming an X.\n";
            }
            case 10 -> {
                return "\nFive tiles of the same type forming a diagonal\n";
            }
            case 11 -> {
                return "\nFive columns of increasing or decreasing height.\nStarting from the first column on the left or on the right,\neach next column must be made of exactly one more tile.\nTiles can be of any type.\n";
            }
        }
        return "error 404 - description not found";
    }
}
