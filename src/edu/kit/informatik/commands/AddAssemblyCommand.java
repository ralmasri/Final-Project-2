package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class AddAssemblyCommand extends Command {

    /**
     * @param factory
     */
    protected AddAssemblyCommand(NeedsAssessment factory) {
        super(factory);
    }

    /* (non-Javadoc)
     * @see edu.kit.informatik.ui.CommandInterface#getNameofCommand()
     */
    @Override
    public String getNameofCommand() {
      return "addAssembly";
    }

    /* (non-Javadoc)
     * @see edu.kit.informatik.ui.CommandInterface#run(java.lang.String)
     */
    @Override
    public String run(String assemblyName) throws InvalidInputException, RuleBrokenException {
        
    }

}
