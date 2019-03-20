package edu.kit.informatik.commands;

import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.ui.CommandInterface;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public abstract class Command implements CommandInterface {

    protected NeedsAssessment factory;
    
    protected Command(final NeedsAssessment factory) {
        this.factory = factory;
    }
}
