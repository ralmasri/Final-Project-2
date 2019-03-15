package edu.kit.informatik.exceptions;

/**
 * Represents a custom exception that is thrown when the user input is invalid.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class InvalidInputException extends Exception {

    private static final long serialVersionUID = 259442120077715015L;
    
    /**
     * Constructor for this custom exception.
     * @param message The error message to be printed.
     */
    public InvalidInputException(String message) {
        super(message);
    }

}
