package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.ui.InputChecker;
import edu.kit.informatik.util.StringList;

/**
 * Represents the removePart command.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class RemovePartCommand extends Command {

    /**
     * Constructor for the removePart command.
     * @param factory All methods are called from it.
     */
    public RemovePartCommand(NeedsAssessment factory) {
        super(factory);
    }

    @Override
    public String getNameofCommand() {
        return "removePart";
    }

    @Override
    public String run(String parameters) throws InvalidInputException, RuleBrokenException {
        InputChecker.checkRemovePart(parameters);
        String[] inputs = parameters.split(StringList.MINUS.toString());
        String nameofAssembly = inputs[0];
        String[] item = inputs[1].split(StringList.COLON.toString());
        factory.removePart(nameofAssembly, Integer.parseInt(item[0]), item[1]);
        return StringList.OK.toString();
    }

}
