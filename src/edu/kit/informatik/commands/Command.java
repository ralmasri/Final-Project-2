package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.ui.CommandInterface;

/**
 * Abstract command class that stores the NeedsAssessment object and from which all commands
 * are extended.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public abstract class Command implements CommandInterface {

    /**
     * The NeedsAssessment object from which all methods to run the program are called from.
     */
    protected NeedsAssessment factory;
    
    /**
     * Constructor for a command.
     * @param factory The object from which all methods to run the program are called from.
     */
    protected Command(final NeedsAssessment factory) {
        this.factory = factory;
    }
}
