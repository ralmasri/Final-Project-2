package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.ui.InputChecker;

/**
 * Represents the getAssemblies command.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class GetAssembliesCommand extends Command {

    /**
     * Constructor for the getAssemblies command.
     * @param factory All methods are called from it.
     */
    public GetAssembliesCommand(NeedsAssessment factory) {
        super(factory);
    }

    @Override
    public String getNameofCommand() {
        return "getAssemblies";
    }

    @Override
    public String run(String parameters) throws InvalidInputException, RuleBrokenException {
        InputChecker.checkOnlyName(parameters);
        return factory.getAssemblies(parameters);
    }

}
