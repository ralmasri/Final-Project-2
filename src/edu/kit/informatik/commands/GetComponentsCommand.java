package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.ui.InputChecker;

/**
 * Represents the getComponents command.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class GetComponentsCommand extends Command {

    /**
     * Constructor for the getComponents command.
     * @param factory All methods are called from it.
     */
    public GetComponentsCommand(NeedsAssessment factory) {
        super(factory);
    }

    @Override
    public String getNameofCommand() {
        return "getComponents";
    }

    @Override
    public String run(String parameters) throws InvalidInputException, RuleBrokenException {
        InputChecker.checkOnlyName(parameters);
        return factory.getComponents(parameters);
    }

}
