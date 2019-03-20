package edu.kit.informatik.ui;

import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public interface CommandInterface {

    String getNameofCommand();
    
    String run(String parameters) throws InvalidInputException, RuleBrokenException;
}
