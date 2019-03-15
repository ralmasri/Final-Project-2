package edu.kit.informatik.exceptions;

/**
 * Represents a custom exception that is thrown when one of the rules of the exercise is broken.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class RuleBrokenException extends Exception {

    private static final long serialVersionUID = -8425682257772899635L;
    
    /**
     * Constructor for this custom exception.
     * @param message The error message to be printed.
     */
    public RuleBrokenException(String message) {
        super(message);
    }

}
