package edu.kit.informatik.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.kit.informatik.data.Item;
import edu.kit.informatik.data.NeedsAssessment;
import edu.kit.informatik.data.TreeNode;
import edu.kit.informatik.exceptions.InvalidInputException;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.ui.InputChecker;
import edu.kit.informatik.util.StringList;

/**
 * Represents the addAssembly command.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class AddAssemblyCommand extends Command {

    /**
     * Constructor for the addAssembly command.
     * @param factory All methods are called from it.
     */
    public AddAssemblyCommand(NeedsAssessment factory) {
        super(factory);
    }
    
    /**
     * Helper method to get the node of an Item.
     * @param amount The amount of the Item.
     * @param name The name of the Item.
     * @return If the item already exists as a tree/assembly, a clone of it will be returned.
     * If it doesn't exist, a new node with the correct amount and name will be returned.
     */
    private TreeNode getChildNode(int amount, String name) {
        TreeNode node = null;
        if (factory.getSystem().getAssembly(name) != null) {
            node = factory.getSystem().getAssembly(name).getCopy();
            node.getData().setAmount(amount);
        } else {
            node = new TreeNode(new Item(amount, name));
        }
        return node;
    }
    
    @Override
    public String getNameofCommand() {
      return "addAssembly";
    }

    @Override
    public String run(String parameters) throws InvalidInputException, RuleBrokenException {
        InputChecker.checkAddAssembly(parameters);
        List<TreeNode> children = new ArrayList<>();
        List<String> names = new ArrayList<>();
        String[] noEquals = parameters.split(StringList.EQUALS.toString());
        String nameAssembly = noEquals[0];
        String[] items = noEquals[1].split(StringList.SEMICOLON.toString());
        for (int index = 0; index < items.length; index++) {
            String[] item = items[index].split(StringList.COLON.toString());
            int amount = Integer.parseInt(item[0]);
            String name = item[1];
            names.add(name);
            children.add(getChildNode(amount, name));
        }
        // This checks for duplicate name entries by the user (HashSet removes duplicates).
        Set<String> setOfNames = new HashSet<>(names);
        if (setOfNames.size() < names.size()) {
            throw new InvalidInputException("you cannot have duplicate parts and/or assemblies as parameters.");
        }
        if (names.contains(nameAssembly)) {
            throw new RuleBrokenException(StringList.CYCLE_ERROR_MSG.toString() + nameAssembly + "-" + nameAssembly);
        }
        factory.addAssembly(nameAssembly, children);
        return StringList.OK.toString();
    }

}
