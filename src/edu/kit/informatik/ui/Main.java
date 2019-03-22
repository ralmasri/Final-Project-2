package edu.kit.informatik.ui;

import java.util.Arrays;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.commands.AddAssemblyCommand;
import edu.kit.informatik.commands.AddPartCommand;
import edu.kit.informatik.commands.GetAssembliesCommand;
import edu.kit.informatik.commands.GetComponentsCommand;
import edu.kit.informatik.commands.PrintAssemblyCommand;
import edu.kit.informatik.commands.RemoveAssemblyCommand;
import edu.kit.informatik.commands.RemovePartCommand;
import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.util.StringList;

/**
 * Main class that executes all the commands and runs the program.
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
        final NeedsAssessment factory = new NeedsAssessment();
        final Collection<CommandInterface> commands = initializeAllCommands(factory);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();
        while (!input.equals(QUIT_COMMAND)) {
            int numberofSpaces = input.length() - input.replaceAll(" ", "").length();
            try {
                if (input.isEmpty() || input.trim().isEmpty()) { // If no input or input with only spaces was entered.
                    throw new InvalidInputException("you must enter a command.");
                }
                final String[] inputArray = input.split(StringList.COMMAND_SEPARATOR.toString());
              // Too many spaces entered or spaces entered for commands without parameters.
                if (numberofSpaces > 1) {
                    throw new InvalidInputException(StringList.TOO_MANY_SPACES.toString());
                }
                final CommandInterface command = commands
                        .stream()
                        .filter(order -> order.getNameofCommand().equals(inputArray[0]))
                        .findAny()
                        .orElseThrow(() -> new InvalidInputException(StringList.COMMAND_DOESNT_EXIST.toString()));
                final String parameters = getParameters(inputArray, command);
                System.out.println(command.run(parameters));
            } catch (RuleBrokenException | InvalidInputException exception) {
                System.out.println("Error, " + exception.getMessage());
            }
            input = br.readLine();
        }
    }
    
    /**
     * Initializes the commands.
     * 
     * @param factory All the methods are called from.
     * @return The list of commands.
     */
    private static Collection<CommandInterface> initializeAllCommands(final NeedsAssessment factory) {
        return Arrays.asList(new AddAssemblyCommand(factory), new AddPartCommand(factory),
                new GetAssembliesCommand(factory), new GetComponentsCommand(factory), 
                new PrintAssemblyCommand(factory), new RemoveAssemblyCommand(factory), 
                new RemovePartCommand(factory));
    }
    
    /**
     * Getter method for parameters.
     * 
     * @param inputArray The array of inputs after being split by the space in the middle.
     * @param command    The command that the user input.
     * @return The parameters. 
     * @throws InvalidInputException If the user input wrong number of parameters.
     */
    private static String getParameters(final String[] inputArray, final CommandInterface command)
            throws InvalidInputException {
        // The input can be a command and its parameter.
        if (inputArray.length > 2) {
            throw new InvalidInputException("wrong input format.");
        }
        if (inputArray.length == 1) { 
                throw new InvalidInputException("you have to give parameters.");
        }
        return inputArray[1];
    }
}
