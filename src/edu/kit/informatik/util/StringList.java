package edu.kit.informatik.util;

/**
 * This class represents strings or regular expressions that I used through out my code.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public enum StringList {

    /**
     * String to be used when command is successful.
     */
    OK("OK"),
    
    /**
     * Error message for when a node is added to a tree that causes a cycle.
     */
    CYCLE_ERROR_MSG("this addition would cause a cycle: "),
    
    /**
     * Error message for when an assembly already exists and a new would like to be added with the same name.
     */
    ASSEMBLY_ALREADY_EXISTS("an assembly of this name already exists"),
    
    /**
     * Separates a command and its parameters.
     */
    COMMAND_SEPARATOR(" ");
    
    /**
     * Textual representation of the output.
     */
    private String output;
    
    /**
     * @param output The String representation of the output.
     */
    StringList(String output) {
        this.output = output;
    }
    
    @Override
    public String toString() {
        return output;
    }
}
