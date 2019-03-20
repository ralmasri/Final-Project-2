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
     * Error message for when the user inputs a command that doesn't exist.
     */
    COMMAND_DOESNT_EXIST("this command doesn't exist."),
    
    /**
     * Error message for when the user inputs too many spaces.
     */
    TOO_MANY_SPACES("only one white space is allowed in the input and "
                            + "it's between a command and its parameters."),
    
    /**
     * Error message for when an assembly already exists and a new would like to be added with the same name.
     */
    ASSEMBLY_ALREADY_EXISTS("an assembly of this name already exists"),
    
    /**
     * Separates a command and its parameters.
     */
    COMMAND_SEPARATOR(" "),
    
    /**
     * Separates a part or assembly and its amount.
     */
    COLON(":"),
    
    /**
     * Separates different parts/assemblies.
     */
    SEMICOLON(";"),
    
    /**
     * Separates the assembly name and the soon to be added item.
     */
    PLUS("\\+"),
    
    /**
     * Separates the assembly name and the soon toe be removed/decreased item.
     */
    MINUS("\\-"),
    
    /**
     * Used to separate a an assembly and its children.
     */
    EQUALS("="),
    
    /**
     * Invalid input format by user.
     */
    WRONG_FORMAT("the paramters you have entered are in the wrong format."),
    
    /**
     * Regex for allowed names.
     */
    ALLOWED_NAMES("[a-zA-Z]+"),
    
    /**
     * Regex for allowed amounts.
     */
    ALLOWED_AMOUNTS("[1-9][0-9]{0,2}|1000|0");
    
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
