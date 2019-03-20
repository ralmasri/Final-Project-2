package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.ui.InputChecker;
import edu.kit.informatik.util.StringList;

/**
 * Represents the removeAssembly command.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class RemoveAssemblyCommand extends Command {

    /**
     * Constructor for the removeAssembly command.
     * @param factory All methods are called from it.
     */
    public RemoveAssemblyCommand(NeedsAssessment factory) {
        super(factory);
    }

    @Override
    public String getNameofCommand() {
        return "removeAssembly";
    }

    @Override
    public String run(String parameters) throws InvalidInputException, RuleBrokenException {
        InputChecker.checkOnlyName(parameters);
        factory.removeAssembly(parameters);
        return StringList.OK.toString();
    }

}
