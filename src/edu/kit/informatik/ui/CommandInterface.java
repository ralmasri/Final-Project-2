package edu.kit.informatik.ui;

import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;

/**
 * Interface for commands. Includes the execution method and a Getter for the name of the command.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public interface CommandInterface {

    /**
     * Getter method for the name of the command.
     * 
     * @return The name of the command.
     */
    String getNameofCommand();
    
    /**
     * Runs the command.
     *  
     * @param parameters The parameters of the command.
     * @return The output.
     * @throws InvalidInputException If the input is not in the correct format.
     * @throws RuleBrokenException If the input breaks any of the rules of the exercise.
     */
    String run(String parameters) throws InvalidInputException, RuleBrokenException;
}
