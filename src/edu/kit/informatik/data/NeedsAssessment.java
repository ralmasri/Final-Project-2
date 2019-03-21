package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.util.StringList;

/**
 * Class that contains all methods to run the program.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */
public class NeedsAssessment {

    /**
     * A list of all trees (assemblies that are not a part of anything). Keys are
     * the roots of the trees.
     */
    private Map<String, Tree> mapofTrees;

    /** List of assemblies that are a part of a tree. */
    private List<TreeNode> listofAssemblies;

    /** Keeps track of all parts. */
    private List<TreeNode> listofParts;

    /** Constructor for the NeedsAssessment class. */
    public NeedsAssessment() {
        this.mapofTrees = new HashMap<>();
        this.listofAssemblies = new ArrayList<>();
        this.listofParts = new ArrayList<>();
    }

    /**
     * Adds an assembly.
     * @param nameOfAssembly The name of the assembly.
     * @param children The child nodes to be added to this new assembly.
     * @throws RuleBrokenException If an assembly of the same name already exists.
     */
    public void addAssembly(String nameOfAssembly, List<TreeNode> children) throws RuleBrokenException {
        boolean doesExist = false;
        List<TreeNode> assemblies = new ArrayList<>();
        if (getNode(nameOfAssembly) != null) {
            throwAssemblyExists();
        }
        for (TreeNode part : listofParts) {
            if (part.getNameofData().equals(nameOfAssembly)) {
                doesExist = true;
                break;
            }
        }
        if (doesExist) {
            for (Tree tree : mapofTrees.values()) {
                TreeNode root = tree.getRootElement();
                List<TreeNode> newAssemblies = tree.findNodes(root, nameOfAssembly, new ArrayList<>());
                if (!newAssemblies.isEmpty()) {
                    assemblies.addAll(newAssemblies);
                    for (TreeNode assembly : newAssemblies) {
                        checkForCycles(assembly, children, tree);
                    }
                }
            }
            addAssemblyThatExists(assemblies, children);
        } else {
            addAssemblyThatDoesntExist(nameOfAssembly, children);
        }     
    }
    
    private void addAssemblyThatDoesntExist(String nameOfAssembly, List<TreeNode> children) throws RuleBrokenException {
        Tree newTree = new Tree(new Item(nameOfAssembly));
        for (TreeNode kid : children) {
            newTree.addNode(kid, newTree.getRootElement(), false);
            if (kid.isLeaf()) {
                listofParts.add(kid);
            } else {
                listofAssemblies.add(kid);
            }
        }
        mapofTrees.put(nameOfAssembly, newTree);
    }

    private void addAssemblyThatExists(List<TreeNode> assemblies, List<TreeNode> children) {
        for (TreeNode assembly : assemblies) {
            for (TreeNode child : children) {
                Tree tree = mapofTrees.get(assembly.getTreeName());
                if (child.isLeaf()) {
                    listofParts.add(child);
                } else {
                    listofAssemblies.add(child);
                }
                tree.addNode(child, assembly, false);
                listofAssemblies.add(assembly);
                listofParts.remove(assembly);
            }
            List<TreeNode> newChildren = createDuplicateChildren(children);
            children.clear();
            children.addAll(newChildren);
        }
    }
    
    private List<TreeNode> createDuplicateChildren(List<TreeNode> children) {
        List<TreeNode> newChildren = new ArrayList<>();
        for (TreeNode child : children) {
            newChildren.add(child.getCopy());
        }
        return newChildren;
    }

    private void checkForCycles(TreeNode assembly, List<TreeNode> children, Tree tree) throws RuleBrokenException {
        List<TreeNode> ancestors = new ArrayList<>();
        List<TreeNode> descendants = new ArrayList<>();
        for (TreeNode child : children) {
            if (assembly.getNameofData().equals(child.getNameofData())) {
                throwCycle(child.getNameofData() + "-" + child.getNameofData());
            }
            ancestors = assembly.getAncestors();
            descendants.add(child);
            descendants.addAll(child.getChildren());
            for (TreeNode descendant : descendants) {
                for (TreeNode ancestor : ancestors) {
                    if (descendant.getNameofData().equals(ancestor.getNameofData())) {
                        throwCycle(createErrorMessage(descendant, ancestor, ancestors, assembly));
                    }
                }
            }
        }
    }

    private String createErrorMessage(TreeNode descendantCycle, TreeNode ancestorCycle, List<TreeNode> ancestors,
            TreeNode assembly) {
        StringBuilder errorMsg = new StringBuilder();
        List<TreeNode> ancestorsOfDescendant = descendantCycle.getAncestors();
        StringBuilder leftOfAssembly = new StringBuilder();
        StringBuilder rightOfAssembly = new StringBuilder();
        for (TreeNode ancestor : ancestors) {
            leftOfAssembly.append("-").append(ancestor.getNameofData());
            if (ancestor.getNameofData().equals(ancestorCycle.getNameofData())) {
                break;
            }
        }
        leftOfAssembly.reverse();
        for (TreeNode node : ancestorsOfDescendant) {
            rightOfAssembly.append("-").append(node.getNameofData());
            if (node.getNameofData().equals(assembly.getNameofData())) {
                break;
            }
        }
        rightOfAssembly.reverse();
        rightOfAssembly.append(descendantCycle.getNameofData());
        String leftError = leftOfAssembly.toString();
        String rightError = rightOfAssembly.toString();
        return errorMsg.append(leftError).append(assembly.getNameofData())
                .append("-").append(rightError).toString();
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
        listofAssembliesCopy.addAll(listofAssemblies);
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
        } else if (mapofTrees.containsKey(nameofAssembly)) {
            removeTree(mapofTrees.get(nameofAssembly).getRootElement());
        } else {
            throw new RuleBrokenException("no BOM exists in the system for the specified name: " + nameofAssembly);
        }
    }

    private void removeTree(TreeNode assembly) {
        for (TreeNode child : assembly.getChildren()) {
            if (!child.isLeaf()) {
                listofAssemblies.remove(child);
                child.getData().setAmount(1);
                Tree childTree = new Tree(child);
                mapofTrees.put(child.getNameofData(), childTree);
            } else {
                listofParts.remove(child);
            }
        }
        Tree tree = mapofTrees.get(assembly.getNameofData());
        mapofTrees.remove(assembly.getNameofData());
        tree.deleteTree();
    }

    private void removeNormalAssembly(TreeNode assembly) {
        for (TreeNode child : assembly.getChildren()) {
            if (!child.isLeaf()) {
                listofAssemblies.remove(child);
                child.getData().setAmount(1);
                Tree childTree = new Tree(child);
                mapofTrees.put(child.getNameofData(), childTree);
            } else {
                listofParts.remove(child);
            }
        }

        listofAssemblies.remove(assembly);
        assembly.deleteChildren();
        listofParts.add(assembly);
    }

    /**
     * Method to create a String of the children of a node.
     * @param nameofAssembly The name of the assembly we want to print.
     * @return A String of the children of the assembly.
     * @throws RuleBrokenException If no such assembly exists
     */
    public String printAssembly(String nameofAssembly) throws RuleBrokenException {
        for (TreeNode part : listofParts) {
            if (part.getNameofData().equals(nameofAssembly)) {
                return "COMPONENT";
            }
        }
        TreeNode assembly = getNode(nameofAssembly);
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
            assemblyBuilder.append(childItem.toString()).append(";");
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
        TreeNode node = getNode(nameofAssembly);
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
            assemblies.append(assembly.toString()).append(";");
        }
        return assemblies.toString().replaceAll(";$", "");
    }

    /**
     * Gets all the components of an assembly in the required format.
     * @param nameofAssembly The name of the assembly we want to get all the
     *                       components from.
     * @return String of all the components.
     * @throws RuleBrokenException If no assembly of that name exists.
     */
    public String getComponents(String nameofAssembly) throws RuleBrokenException {
        TreeNode node = getNode(nameofAssembly);
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
            components.append(assembly.toString()).append(";");
        }
        return components.toString().replaceAll(";$", "");
    }
    
    public void addPart(String nameofAssembly, int amount, String name) throws RuleBrokenException {
        List<TreeNode> nodes = new ArrayList<>();
        if (mapofTrees.containsKey(nameofAssembly)) {
            nodes.add(mapofTrees.get(nameofAssembly).getRootElement());
        } else {
            for (Tree tree : mapofTrees.values()) {
                List<TreeNode> foundNodes = tree.findNodes(tree.getRootElement(), nameofAssembly, new ArrayList<>());
                if (!foundNodes.isEmpty() && !foundNodes.get(0).isLeaf()) {
                    nodes.addAll(foundNodes);
                }
            }
        }
        if (nodes.isEmpty()) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        TreeNode child = null;
        if (getNode(name) == null) {
            child = new TreeNode(new Item(amount, name));
            listofParts.add(child);
        } else {
            child = getNode(name).getCopy();
        }
        checkForCycles(nodes, child);
        if (child.getAmountofData() + amount > 1000) {
            throw new RuleBrokenException("the amount of a part or assembly cannot exceed 1000.");
        }
        boolean isAChild = false;
        int index = 0;
        for (TreeNode kid : nodes.get(0).getChildren()) {
            if (kid.getNameofData().equals(name)) {
                isAChild = true;
                index = nodes.get(0).getChildren().indexOf(kid);
                break;
            }
        }
        if (isAChild) {
            for (TreeNode assembly : nodes) {
                TreeNode toBeIncreased = assembly.getChildren().get(index);
                toBeIncreased.getData().setAmount(toBeIncreased.getAmountofData() + amount);
            }
        } else {
            for (TreeNode assembly : nodes) {
                assembly.addChild(child);
            }
        }
    }
    
    public void removePart(String nameofAssembly, int amount, String name) throws RuleBrokenException {
        boolean isATree = false;
        List<TreeNode> nodes = new ArrayList<>();
        if (mapofTrees.containsKey(nameofAssembly)) {
            nodes.add(mapofTrees.get(nameofAssembly).getRootElement());
            isATree = true;
        } else {
            for (Tree tree : mapofTrees.values()) {
                List<TreeNode> foundNodes = tree.findNodes(tree.getRootElement(), nameofAssembly, new ArrayList<>());
                if (!foundNodes.isEmpty()) {
                    nodes.addAll(foundNodes);
                }
            }
        }
        if (nodes.isEmpty()) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        TreeNode child = null;
        if (getNode(name) == null) {
            for (TreeNode part : listofParts) {
                if (part.getNameofData().equals(name) && part.getParent().getNameofData().equals(nameofAssembly)) {
                    child = part;
                    break;
                }
            }
        }
        if (child == null) {
            throwDoesntExist(name);
        }
        boolean isContained = false;
        int index = 0;
        for (TreeNode kid : nodes.get(0).getChildren()) {
            if (kid.getNameofData().equals(name)) {
                if (kid.getAmountofData() >= amount) {
                    isContained = true;
                    index = nodes.get(0).getChildren().indexOf(kid);
                    break;
                } else {
                    throw new RuleBrokenException(nameofAssembly + " doesn't contain " + name 
                            + " in the specified amount: " + amount + ".");
                }
            } else {
                throw new RuleBrokenException(nameofAssembly + " doesn't contain " + name + ".");
            }
        }
        for (TreeNode node : nodes) {
            TreeNode tobeRemoved = node.getChildren().get(index);
            if (tobeRemoved.getAmountofData() - amount == 0) {
                if (isATree && node.getChildren().size() == 1) {
                    Tree tree = mapofTrees.get(nameofAssembly);
                    mapofTrees.remove(nameofAssembly);
                    tree.deleteTree();
                    return;
                } else if (node.getChildren().size() == 1) {
                    listofAssemblies.remove(node);
                    listofParts.add(node);
                    node.deleteChildren();
                }
            } else {
                tobeRemoved.getData().setAmount(tobeRemoved.getAmountofData() - amount);
            }
        }
    }
    
    private void checkForCycles(List<TreeNode> nodes, TreeNode child) throws RuleBrokenException {
        for (TreeNode node : nodes) {
            Tree tree = mapofTrees.get(node.getTreeName());
            String errorMsg = tree.getCycleErrorMessage(node, child);
            if (!errorMsg.isEmpty()) {
                if (errorMsg.length() == 3) {
                    throwCycle(errorMsg);
                } else {
                    throwCycle(child.getNameofData() + errorMsg); 
                }
            }
        }
    }
    
    public TreeNode getNode(String nameofNode) {
        if (mapofTrees.containsKey(nameofNode)) {
            return mapofTrees.get(nameofNode).getRootElement();
        }
        for (TreeNode assembly : listofAssemblies) {
            if (assembly.getNameofData().equals(nameofNode)) {
                return assembly;
            }
        }
        return null;
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
}
