package edu.kit.informatik.ui;

import java.util.regex.Pattern;

import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.util.StringList;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class InputChecker {

    private static final String ADDASSEMBLY_REGEX = StringList.ALLOWED_NAMES.toString()
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
            + ")*";
    private static final Pattern ADDASSEMBLY_PATTERN = Pattern.compile(ADDASSEMBLY_REGEX);
    private static final Pattern ONLY_NAME_PATTERN = Pattern.compile(StringList.ALLOWED_NAMES.toString());
    private static final Pattern ADDPART_PATTERN = Pattern.compile(StringList.ALLOWED_NAMES.toString()
            + StringList.PLUS.toString()
            + StringList.ALLOWED_AMOUNTS.toString()
            + StringList.COLON.toString()
            + StringList.ALLOWED_NAMES.toString());
    private static final Pattern REMOVEPART_PATTERN = Pattern.compile(StringList.ALLOWED_NAMES.toString()
            + StringList.MINUS.toString()
            + StringList.ALLOWED_AMOUNTS.toString()
            + StringList.COLON.toString()
            + StringList.ALLOWED_NAMES.toString());
    
    
    public static String checkAddAssembly(final String input) throws InvalidInputException {
        if (!ADDASSEMBLY_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    public static String checkOnlyName(final String input) throws InvalidInputException {
        if (!ONLY_NAME_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    public static String checkAddPart(final String input) throws InvalidInputException {
        if (!ADDPART_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    public static String checkRemovePart(final String input) throws InvalidInputException {
        if (!REMOVEPART_PATTERN.matcher(input).matches()) {
            throwWrongFormat();
        }
        return input;
    }
    
    private static void throwWrongFormat() throws InvalidInputException {
        throw new InvalidInputException(StringList.WRONG_FORMAT.toString());
    }
}
