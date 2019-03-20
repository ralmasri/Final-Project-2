package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.ui.InputChecker;
import edu.kit.informatik.util.StringList;

/**
 * Represents the addPart command.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class AddPartCommand extends Command {

    /**
     * Constructor for the addPart command.
     * @param factory All methods are called from it.
     */
    public AddPartCommand(NeedsAssessment factory) {
        super(factory);
    }

    @Override
    public String getNameofCommand() {
        return "addPart";
    }

    @Override
    public String run(String parameters) throws InvalidInputException, RuleBrokenException {
        InputChecker.checkAddPart(parameters);
        String[] inputs = parameters.split(StringList.PLUS.toString());
        String nameofAssembly = inputs[0];
        String[] item = inputs[1].split(StringList.COLON.toString());
        factory.addPart(nameofAssembly, Integer.parseInt(item[0]), item[1]);
        return StringList.OK.toString();
     }

}
