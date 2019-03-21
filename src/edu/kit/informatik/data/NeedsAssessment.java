package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.util.StringList;

/**
 * Class that contains all methods to run the program.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */
public class NeedsAssessment {

    /** The index of the first instance of an item.
     * It is used when it doesn't matter which instance is called.
     */
    private static final int FIRST_INDEX = 0;
    
    /** The amount of an item when it is going to vanish (a.k.a removing all the item). */
    private static final int VANISHING_AMOUNT = 0;
    
    /** The system that stores all the information regarding the parts/assemblies. */
    private SystemInitializer system;

    /** Constructor for the NeedsAssessment class. */
    public NeedsAssessment() {
        this.system = new SystemInitializer();
    }

    /**
     * Adds an assembly.
     * @param nameOfAssembly The name of the assembly.
     * @param children       The child nodes to be added to this new assembly.
     * @throws RuleBrokenException If an assembly of the same name already exists or if a cycle occurs because
     * of the addition.
     */
    public void addAssembly(String nameOfAssembly, List<TreeNode> children) throws RuleBrokenException {
        boolean doesExist = false; // If the part we want to turn into an assembly exists in the system.
        List<TreeNode> assemblies = new ArrayList<>();
        if (system.getAssembly(nameOfAssembly) != null) {
            throwAssemblyExists();
        }
        for (TreeNode part : system.getListofParts()) {
            if (part.getNameofData().equals(nameOfAssembly)) {
                doesExist = true;
                break;
            }
        }
        if (doesExist) {
            for (Tree tree : system.getMapofTrees().values()) {
                TreeNode root = tree.getRootElement();
                List<TreeNode> newAssemblies = tree.findNodes(root, nameOfAssembly, new ArrayList<>());
                if (!newAssemblies.isEmpty()) {
                    assemblies.addAll(newAssemblies);
                    for (TreeNode assembly : newAssemblies) {
                        String errorMsg = Tree.checkForCycles(assembly, children);
                        if (!errorMsg.isEmpty()) {
                            throwCycle(errorMsg);
                        }
                    }
                }
            }
            addAssemblyThatExists(assemblies, children);
        } else {
            addAssemblyThatDoesntExist(nameOfAssembly, children);
        }
    }

    /**
     * Helper method to add an assembly for an item that doesn't exist in the
     * system.
     * @param nameOfAssembly The name of said item.
     * @param children       The children to be added.
     */
    private void addAssemblyThatDoesntExist(String nameOfAssembly, List<TreeNode> children) throws RuleBrokenException {
        Tree newTree = new Tree(new Item(nameOfAssembly));
        for (TreeNode kid : children) {
            newTree.addNode(kid, newTree.getRootElement(), false);
            if (kid.isLeaf()) {
                system.getListofParts().add(kid);
            } else {
                system.getListofAssemblies().add(kid);
            }
        }
        system.getMapofTrees().put(nameOfAssembly, newTree);
    }

    /**
     * Helper method to add an assembly for an item that exists in the system.
     * @param assemblies The list of all instances of said item in the system.
     * @param children   The children to be added.
     */
    private void addAssemblyThatExists(List<TreeNode> assemblies, List<TreeNode> children) {
        for (TreeNode assembly : assemblies) {
            for (TreeNode child : children) {
                Tree tree = system.getMapofTrees().get(assembly.getTreeName());
                if (child.isLeaf()) {
                    system.getListofParts().add(child);
                } else {
                    system.getListofAssemblies().add(child);
                }
                tree.addNode(child, assembly, false);
                system.getListofAssemblies().add(assembly);
                system.getListofParts().remove(assembly);
            }
            List<TreeNode> newChildren = createDuplicateChildren(children);
            children.clear();
            children.addAll(newChildren);
        }
    }

    /**
     * Creates clones of children nodes.
     * @param children The children to be cloned.
     * @return A list of cloned children.
     */
    private List<TreeNode> createDuplicateChildren(List<TreeNode> children) {
        List<TreeNode> newChildren = new ArrayList<>();
        for (TreeNode child : children) {
            newChildren.add(child.getCopy());
        }
        return newChildren;
    }

    /**
     * Method to remove an assembly. If the assembly is "super", then it will be
     * deleted from the system, otherwise all its children will be deleted and it
     * will be treated as a component.
     * @param nameofAssembly The name of the assembly to be deleted.
     * @throws RuleBrokenException If no such assembly exists in the system.
     */
    public void removeAssembly(String nameofAssembly) throws RuleBrokenException {
        boolean isanAssembly = false;
        List<TreeNode> listofAssembliesCopy = new ArrayList<>();
        listofAssembliesCopy.addAll(system.getListofAssemblies());
        Iterator<TreeNode> iter = listofAssembliesCopy.iterator();
        while (iter.hasNext()) {
            TreeNode assembly = iter.next();
            if (assembly.getNameofData().equals(nameofAssembly)) {
                removeNormalAssembly(assembly);
                isanAssembly = true;
            }
        }
        if (isanAssembly) {
            return;
        } else if (system.getMapofTrees().containsKey(nameofAssembly)) {
            removeTree(system.getMapofTrees().get(nameofAssembly).getRootElement());
        } else {
            throw new RuleBrokenException("no BOM exists in the system for the specified name: " + nameofAssembly);
        }
    }

    /**
     * Helper remove method for a "super" assembly (an assembly that is the root of a tree).
     * @param assembly The assembly.
     */
    private void removeTree(TreeNode assembly) {
        for (TreeNode child : assembly.getChildren()) {
            if (!child.isLeaf()) {
                system.getListofAssemblies().remove(child);
                child.getData().setAmount(1);
                Tree childTree = new Tree(child);
                system.getMapofTrees().put(child.getNameofData(), childTree);
            } else {
                system.getListofParts().remove(child);
            }
        }
        Tree tree = system.getMapofTrees().get(assembly.getNameofData());
        system.getMapofTrees().remove(assembly.getNameofData());
        tree.deleteTree();
    }

    /**
     * Helper remove method for a normal assembly (an assembly that is a part of a
     * tree).
     * @param assembly The assembly.
     */
    private void removeNormalAssembly(TreeNode assembly) {
        for (TreeNode child : assembly.getChildren()) {
            if (!child.isLeaf()) {
                system.getListofAssemblies().remove(child);
                child.getData().setAmount(1);
                Tree childTree = new Tree(child);
                system.getMapofTrees().put(child.getNameofData(), childTree);
            } else {
                system.getListofParts().remove(child);
            }
        }
        system.getListofAssemblies().remove(assembly);
        assembly.deleteChildren();
        system.getListofParts().add(assembly);
    }

    /**
     * Method to create a String of the children of a node.
     * @param nameofAssembly The name of the assembly we want to print.
     * @return A String of the children of the assembly.
     * @throws RuleBrokenException If no such assembly exists
     */
    public String printAssembly(String nameofAssembly) throws RuleBrokenException {
        for (TreeNode part : system.getListofParts()) {
            if (part.getNameofData().equals(nameofAssembly)) {
                return "COMPONENT";
            }
        }
        TreeNode assembly = system.getAssembly(nameofAssembly);
        if (assembly == null) {
            throwDoesntExist(nameofAssembly);
        }
        List<TreeNode> children = assembly.getChildren();
        List<Item> childrenItems = new ArrayList<>();
        for (TreeNode child : children) {
            childrenItems.add(child.getData());
        }
        Collections.sort(childrenItems, new ItemComparator());
        StringBuilder assemblyBuilder = new StringBuilder();
        for (Item childItem : childrenItems) {
            assemblyBuilder.append(childItem.toString()).append(StringList.SEMICOLON.toString());
        }
        return assemblyBuilder.toString().replaceAll(";$", "");
    }

    /**
     * Gets all the assemblies of an assembly in the required format.
     * @param nameofAssembly The name of the assembly we want to get all others from.
     * @return String of all the assemblies or "EMPTY" if the assembly is only made up of parts.
     * @throws RuleBrokenException If no assembly of that name exists.
     */
    public String getAssemblies(String nameofAssembly) throws RuleBrokenException {
        TreeNode node = system.getAssembly(nameofAssembly);
        if (node == null) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        if (node.areChildrenComponents()) {
            return "EMPTY";
        }
        List<TreeNode> listofAssemblies = new ArrayList<>();
        for (TreeNode descendant : node.getDescendants()) {
            if (!descendant.isLeaf()) {
                listofAssemblies.add(descendant);
            }
        }
        Collections.sort(listofAssemblies, new NodeComparator());
        StringBuilder assemblies = new StringBuilder();
        for (TreeNode assembly : listofAssemblies) {
            assemblies.append(assembly.toString()).append(StringList.SEMICOLON.toString());
        }
        return assemblies.toString().replaceAll(";$", "");
    }

    /**
     * Gets all the components of an assembly in the required format.
     * @param nameofAssembly The name of the assembly we want to get all the components from.
     * @return String of all the components.
     * @throws RuleBrokenException If no assembly of that name exists.
     */
    public String getComponents(String nameofAssembly) throws RuleBrokenException {
        TreeNode node = system.getAssembly(nameofAssembly);
        if (node == null) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        List<TreeNode> listofComponents = new ArrayList<>();
        for (TreeNode descendant : node.getDescendants()) {
            if (descendant.isLeaf()) {
                listofComponents.add(descendant);
            }
        }
        Collections.sort(listofComponents, new NodeComparator());
        StringBuilder components = new StringBuilder();
        for (TreeNode assembly : listofComponents) {
            components.append(assembly.toString()).append(StringList.SEMICOLON.toString());
        }
        return components.toString().replaceAll(";$", "");
    }

    /**
     * Add part method. Adds an assembly or part to an existing assembly.
     * @param nameofAssembly The name of the assembly to which an item is going to be added.
     * @param amount The amount of the item to be added.
     * @param name The name of the item to be added.
     * @throws RuleBrokenException If no assembly exists, if the amount to be added is larger than the max, or
     * if the addition causes a cycle.
     */
    public void addPart(String nameofAssembly, int amount, String name) throws RuleBrokenException {
        List<TreeNode> nodes = system.getAllAssembliesOfName(nameofAssembly);
        if (nodes.isEmpty()) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        boolean isAChild = false;
        int index = 0;
        for (TreeNode child : nodes.get(FIRST_INDEX).getChildren()) {
            if (child.getNameofData().equals(name)) {
                isAChild = true;
                index = nodes.get(FIRST_INDEX).getChildren().indexOf(child);
                break;
            }
        }
        if (isAChild) {
            for (TreeNode node : nodes) {
                int newAmount = node.getChildren().get(index).getAmountofData() + amount;
                if (newAmount > SystemInitializer.getMaxAmount()) {
                    throwAmount();
                }
                node.getChildren().get(index).getData().setAmount(newAmount);
            }
        } else {
            addPartThatIsntChild(name, amount, nodes);
        }
    }

    /**
     * Helper method for addPart that is called when adding an item that isn't already a child
     * of the assembly.
     * @param name The name of the item.
     * @param amount The amount of the item.
     * @param nodes All instances of the assembly we want to add said item to.
     * @throws RuleBrokenException If a cycle would occurs because of said addition.
     */
    private void addPartThatIsntChild(String name, int amount, List<TreeNode> nodes) throws RuleBrokenException {
        TreeNode nodeToBeAdded = null;
        boolean isATree = false;
        boolean isAssembly = false;
        if (system.getAssembly(name) != null) {
            if (system.getMapofTrees().containsKey(name)) {
                isATree = true;
            } else {
                isAssembly = true;
            }
            nodeToBeAdded = system.getAssembly(name);
        } else if (system.getPart(name) != null) {
            nodeToBeAdded = system.getPart(name);
        } else {
            nodeToBeAdded = new TreeNode(new Item(amount, name));
        }
        checkAddPartCycles(nodes, new ArrayList<>(Arrays.asList(nodeToBeAdded)));
        for (TreeNode node : nodes) {
            TreeNode actualNode = nodeToBeAdded.getCopy();
            actualNode.getData().setAmount(amount);
            node.addChild(actualNode);
            if (isATree) {
                system.getMapofTrees().get(nodeToBeAdded.getNameofData()).deleteTree();
                system.getMapofTrees().remove(nodeToBeAdded.getNameofData());
                system.getListofAssemblies().add(actualNode);
            } else if (isAssembly) {
                system.getListofAssemblies().add(actualNode);
            } else {
                system.getListofParts().add(actualNode);
            }
        }
    }

    /**
     * Remove part method. Removes a certain amount of a child of an assembly.
     * @param nameofAssembly The name of the assembly.
     * @param amount The amount to be removed.
     * @param name The name of the item to be removed.
     * @throws RuleBrokenException If no assembly of nameofAssembly exists, if no item exists of said name, 
     * if the child doesn't exists in said amount or if the item isn't a child of the assembly.
     */
    public void removePart(String nameofAssembly, int amount, String name) throws RuleBrokenException {
        boolean isATree = system.getMapofTrees().containsKey(nameofAssembly);
        List<TreeNode> nodes = system.getAllAssembliesOfName(nameofAssembly);
        if (nodes.isEmpty()) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        TreeNode child = null;
        if (system.getAssembly(name) == null) {
            for (TreeNode part : system.getListofParts()) {
                if (part.getNameofData().equals(name) && part.getParent().getNameofData().equals(nameofAssembly)) {
                    child = part;
                    break;
                }
            }
        }
        if (child == null) {
            throwDoesntContain(nameofAssembly, name);
        }
        removePartExecutor(nodes, isATree, amount, name, nameofAssembly);
    }
    
    /**
     * Helper method that executes the removePart command. Split from the main method for better readability.
     * @param nodes All instances of the assembly node.
     * @param isATree true if the assembly is a tree, otherwise false.
     * @param amount The amount to be deducted.
     * @param name The name of the child to be deducted.
     * @param nameofAssembly The name of the assembly.
     * @throws RuleBrokenException If the parent doesn't contain the child in the specified amount or at all.
     */
    private void removePartExecutor(List<TreeNode> nodes, boolean isATree, int amount, String name
            , String nameofAssembly) throws RuleBrokenException {
        int index = getIndexOfRemoved(nodes, name, amount, nameofAssembly);
        for (TreeNode node : nodes) { 
            TreeNode tobeRemoved = node.getChildren().get(index);
            if (tobeRemoved.getAmountofData() - amount == VANISHING_AMOUNT) {
                if (isATree && node.getChildren().size() == 1) {
                    Tree tree = system.getMapofTrees().get(nameofAssembly);
                    system.getMapofTrees().remove(nameofAssembly);
                    tree.deleteTree();
                    return;
                } else if (node.getChildren().size() == 1) {
                    system.getListofAssemblies().remove(node);
                    system.getListofParts().add(node);
                    node.deleteChildren();
                }
            } else {
                tobeRemoved.getData().setAmount(tobeRemoved.getAmountofData() - amount);
            }
        }
    }
    
    /**
     * Helper method to get the index of the node to be removed in the children array list of the parent node.
     * @param nodes List of all instances of the parent node.
     * @param name The name of the node to be removed/deducted.
     * @param amount The amount to be deducted.
     * @param nameofAssembly The name of the parent node/assembly.
     * @return The index.
     * @throws RuleBrokenException If the parent doesn't contain the child in the specified amount or at all.
     */
    private int getIndexOfRemoved(List<TreeNode> nodes, String name, int amount, String nameofAssembly) 
            throws RuleBrokenException {
        int index = 0;
        for (TreeNode kid : nodes.get(FIRST_INDEX).getChildren()) {
            if (kid.getNameofData().equals(name)) {
                if (kid.getAmountofData() >= amount) {
                    index = nodes.get(FIRST_INDEX).getChildren().indexOf(kid);
                    break;
                } else {
                    throw new RuleBrokenException(
                            nameofAssembly + " doesn't contain " + name + " in the specified amount: " + amount + ".");
                }
            } else {
                throwDoesntContain(nameofAssembly, name);
            }
        }
        return index;
    }

    /**
     * Helper method to check for cycles for the addPart method.
     * @param nodes List of all instances of the assembly to which the children will be added.
     * @param children The children to be added.
     * @throws RuleBrokenException If a cycle occurs because of the addition.
     */
    private void checkAddPartCycles(List<TreeNode> nodes, List<TreeNode> children) throws RuleBrokenException {
        for (TreeNode node : nodes) {
            String errorMsg = Tree.checkForCycles(node, children);
            if (!errorMsg.isEmpty()) {
                throwCycle(errorMsg);
            }
        }
    }
    
    /** @return The system. */
    public SystemInitializer getSystem() {
        return system;
    }
    
    private void throwAssemblyExists() throws RuleBrokenException {
        throw new RuleBrokenException(StringList.ASSEMBLY_ALREADY_EXISTS.toString());
    }

    private void throwAssemblyDoesntExist(String name) throws RuleBrokenException {
        throw new RuleBrokenException("no BOM exists in the system for the specified name: " + name + ".");
    }

    private void throwDoesntExist(String name) throws RuleBrokenException {
        throw new RuleBrokenException("no BOM or component exists in the system for the specified name: " + name + ".");
    }

    private void throwCycle(String msg) throws RuleBrokenException {
        throw new RuleBrokenException(StringList.CYCLE_ERROR_MSG.toString() + msg);
    }

    private void throwAmount() throws RuleBrokenException {
        throw new RuleBrokenException("the amount of a part/assembly cannot exceed 1000.");
    }
    
    private void throwDoesntContain(String nameofAssembly, String name) throws RuleBrokenException {
        throw new RuleBrokenException(nameofAssembly + " doesn't contain " + name + ".");
    }
}