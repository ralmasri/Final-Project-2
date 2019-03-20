package edu.kit.informatik.ui;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.util.StringList;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class Main {

    /** The command that terminates the program. */
    public static final String QUIT_COMMAND = "quit";
    
    /**
     * Main method to execute the program.
     * @param args Arguments.
     */
    public static void main(String[] args) {
        String input = Terminal.readLine();
        while (!input.equals(QUIT_COMMAND)) {
            int numberofSpaces = input.length() - input.replaceAll(" ", "").length();
            try {
                if (input.isEmpty() || input.trim().isEmpty()) { // If no input or input with only spaces was entered.
                    throw new InvalidInputException("you must enter a command.");
                }
                final String[] inputArray = input.split(StringList.COMMAND_SEPARATOR.toString());
            } catch (RuleBrokenException | InvalidInputException exception) {
                Terminal.printError(exception.getMessage());
            }
        }
    }
}
