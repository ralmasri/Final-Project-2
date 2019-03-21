package edu.kit.informatik.ui;

import java.util.regex.Pattern;

import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.util.StringList;

/**
 * Class that includes methods to check different inputs for correct format.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class InputChecker {

    /** Pattern for the addAssembly command's parameters. */
    private static final Pattern ADDASSEMBLY_PATTERN = Pattern.compile(StringList.ALLOWED_NAMES.toString()
            + StringList.EQUALS.toString()
            + "("
            + StringList.ALLOWED_AMOUNTS.toString()
            + StringList.COLON.toString()
            + StringList.ALLOWED_NAMES.toString()
            + ")("
            + "(" + StringList.SEMICOLON.toString() + ")"
            + StringList.ALLOWED_AMOUNTS.toString()
            + StringList.COLON.toString()
            + StringList.ALLOWED_NAMES.toString()
            + ")*");
    
    /** Pattern for commands that have only a name as a parameter. */
    private static final Pattern ONLY_NAME_PATTERN = Pattern.compile(StringList.ALLOWED_NAMES.toString());
    
    /** Pattern for the addPart command's parameters. */
    private static final Pattern ADDPART_PATTERN = Pattern.compile(StringList.ALLOWED_NAMES.toString()
            + StringList.PLUS.toString()
            + StringList.ALLOWED_AMOUNTS.toString()
            + StringList.COLON.toString()
            + StringList.ALLOWED_NAMES.toString());
    
    /** Pattern for the removePart command's parameters. */
    private static final Pattern REMOVEPART_PATTERN = Pattern.compile(StringList.ALLOWED_NAMES.toString()
            + StringList.MINUS.toString()
            + StringList.ALLOWED_AMOUNTS.toString()
            + StringList.COLON.toString()
            + StringList.ALLOWED_NAMES.toString());
    
    /**
     * Method to check if the addAssembly command's parameters are in the correct format.
     * @param input The parameters entered by the user.
     * @return The parameters.
     * @throws InvalidInputException If the input is not in the correct format.
     */
    public static String checkAddAssembly(final String input) throws InvalidInputException {
        if (!ADDASSEMBLY_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    /**
     * Method to check if the name is in the correct format. 
     * It is used for commands that have only a name as a parameter.
     * @param input The parameter entered by the user.
     * @return The parameter.
     * @throws InvalidInputException If the input is not in the correct format.
     */
    public static String checkOnlyName(final String input) throws InvalidInputException {
        if (!ONLY_NAME_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    /**
     * Method to check if the parameters for the addPart command are in the correct format.
     * @param input The parameters entered by the user.
     * @return The parameters.
     * @throws InvalidInputException If the input is not in the correct format.
     */
    public static String checkAddPart(final String input) throws InvalidInputException {
        if (!ADDPART_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    /**
     * Method to check if the parameters for the removePart command are in the correct format.
     * @param input The parameters entered by the user.
     * @return The parameters.
     * @throws InvalidInputException If the input is not in the correct format.
     */
    public static String checkRemovePart(final String input) throws InvalidInputException {
        if (!REMOVEPART_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    /**
     * @throws InvalidInputException If the input is in the wrong format.
     */
    private static void throwWrongFormat() throws InvalidInputException {
        throw new InvalidInputException(StringList.WRONG_FORMAT.toString());
    }
}
