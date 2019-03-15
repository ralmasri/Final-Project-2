package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.util.StringList;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class NeedsAssessment {

    /**
     * A list of all trees (assemblies that are not a part of anything). Keys are
     * the roots of the trees.
     */
    private Map<TreeNode, Tree> mapofTrees;

    /** List of assemblies that are a part of a tree. */
    private List<TreeNode> listofAssemblies;

    /**
     * Keeps track of all parts
     */
    private List<Item> listofParts;

    /**
     * Constructor for the needs assessment class.
     */
    public NeedsAssessment() {
        this.mapofTrees = new HashMap<>();
        this.listofAssemblies = new ArrayList<>();
        this.listofParts = new ArrayList<>();
    }

    // TODO make sure that input has only one type of item being added and limit is
    // 1000
    // TODO remove the equals sign and semicolon
    // TODO Make sure that the same items are not being added

    /**
     * Adds an assembly.
     * 
     * @param nameOfAssembly The name of the assembly.
     * @param childItemNames The item names to be added.
     * @throws RuleBrokenException If an assembly of the same name already exists.
     */
    public void addAssembly(String nameOfAssembly, String[] childItemNames) throws RuleBrokenException {
        TreeNode newAssembly = new TreeNode(new Item(nameOfAssembly));
        if (mapofTrees.containsKey(newAssembly)) {
            throwAssemblyExists();
        }
        for (TreeNode assembly : listofAssemblies) {
            if (assembly.equals(newAssembly)) {
                throwAssemblyExists();
            }
        }
        // If the item is a part that already exists
        if (listofParts.contains(newAssembly.getData())) {
            for (Tree tree : mapofTrees.values()) {
                List<TreeNode> nodesOfNewAssembly = tree.getAllNodes().stream().filter(node -> node.equals(newAssembly))
                        .collect(Collectors.toList());
                for (TreeNode node : nodesOfNewAssembly) {
                    addChildItems(childItemNames, node);
                }
            }
            listofAssemblies.add(newAssembly);
        } else {
            // If the new assembly doesn't exist in the system then it will made into a
            // "super"
            // assembly (a.k.a a tree)
            Tree newTree = new Tree(newAssembly.getData());
            newTree.addNode(newAssembly);
            mapofTrees.put(newAssembly, newTree);
            addChildItems(childItemNames, newAssembly);
        }
    }

    /**
     * Helper method for addAssembly to add all the children to a node.
     * 
     * @param childItemNames All the names of the children.
     * @param node           The parent node to these children.
     */
    private void addChildItems(String[] childItemNames, TreeNode node) {
        for (String nameofItem : childItemNames) {
            Item childItem = new Item(nameofItem);
            node.addChild(new TreeNode(childItem));
            listofParts.add(childItem);
        }
    }

    /**
     * Method to remove an assembly. If the assembly is "super", then it will be
     * deleted from the system, otherwise all its children will be deleted and it
     * will be treated as a component.
     * 
     * @param nameofAssembly The name of the assembly to be deleted.
     * @throws RuleBrokenException If no such assembly exists in the system.
     */
    public void removeAssembly(String nameofAssembly) throws RuleBrokenException {
        TreeNode assembly = new TreeNode(new Item(nameofAssembly));
        if (mapofTrees.containsKey(assembly)) {
            removeTree(assembly);
        } else if (listofAssemblies.contains(assembly)) {
            removeNormalAssembly(assembly);
        } else {
            throw new RuleBrokenException("no BOM exists in the system for the specified name: " + nameofAssembly);
        }
    }

    private void removeTree(TreeNode assembly) {
        for (Tree tree : mapofTrees.values()) {
            TreeNode root = tree.getRootElement();
            if (root.equals(assembly)) {
                for (TreeNode treenode : tree.getAllNodes()) {
                    if (!treenode.isLeaf()) {
                        treenode.getData().setAmount(1);
                        Tree childTree = new Tree(treenode);
                        mapofTrees.put(treenode, childTree);
                    }
                }
                mapofTrees.remove(tree.getRootElement());
                root = null;
                return;
            }
        }
    }

    private void removeNormalAssembly(TreeNode assembly) {
        for (Tree superAssembly : mapofTrees.values()) {
            for (TreeNode node : superAssembly.getAllNodes()) {
                if (node.equals(assembly)) {
                    listofAssemblies.remove(node);
                    listofParts.add(node.getData());
                    for (TreeNode child : node.getChildren()) {
                        if (!child.isLeaf()) {
                            child.getData().setAmount(1);
                            Tree childTree = new Tree(child);
                            mapofTrees.put(child, childTree);
                        }
                    }
                    node.deleteChildren();
                }
            }
        }
        return;
    }

    /**
     * Method to create a String of the children of a node.
     * @param nameofAssembly The name of the assembly we want to print.
     * @return A String of the children of the assembly.
     * @throws RuleBrokenException If no such assembly exists
     */
    public String printAssembly(String nameofAssembly) throws RuleBrokenException {
        Item assemblyItem = new Item(nameofAssembly);
        TreeNode assemblyItemNode = new TreeNode(assemblyItem);
        if (!listofParts.contains(assemblyItem) || !mapofTrees.containsKey(assemblyItemNode)
                || listofAssemblies.contains(assemblyItemNode)) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        if (listofParts.contains(assemblyItem)) {
            return "COMPONENT";
        }
        TreeNode actualNode = null;
        if (mapofTrees.containsKey(assemblyItemNode)) {
             actualNode = mapofTrees.keySet().stream().filter(node -> node.equals(assemblyItemNode))
                     .findFirst().get();
        } else {
            actualNode = listofAssemblies.stream().filter(node -> node.equals(assemblyItemNode))
                    .findFirst().get();
        }
        assert actualNode != null;
        List<TreeNode> children = actualNode.getChildren();
        List<Item> childrenItems = new ArrayList<>();
        for (TreeNode child : children) {
            childrenItems.add(child.getData());
        }
        Collections.sort(childrenItems, new ItemComparator());
        StringBuilder assemblyStringB = new StringBuilder();
        for (Item childItem : childrenItems) {
            assemblyStringB.append(childItem.toString()).append(";");
        }
        return assemblyStringB.toString().replaceAll(";$", "");
    }
    
    /**
     * Gets all the assemblies of an assembly in the required format.
     * @param nameofAssembly The name of the assembly we want to get all others from.
     * @return String of all the assemblies or "EMPTY" if the assembly is only made up of  parts.
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
        List<TreeNode> listofDescendants = node.getDescendantAssemblies();
        Collections.sort(listofDescendants, new NodeComparator());
        StringBuilder assemblies = new StringBuilder();
        for (TreeNode descendant : listofDescendants) {
            assemblies.append(descendant.toString()).append(";");
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
        TreeNode node = getNode(nameofAssembly);
        if (node == null) {
            throwAssemblyDoesntExist(nameofAssembly);
        }
        List<TreeNode> listofDescendants = node.getDescendantComponents();
        Collections.sort(listofDescendants, new NodeComparator());
        StringBuilder assemblies = new StringBuilder();
        for (TreeNode descendant : listofDescendants) {
            assemblies.append(descendant.toString()).append(";");
        }
        return assemblies.toString().replaceAll(";$", "");
    }
    
    private TreeNode getNode(String name) {
        Item assemblyItem = new Item(name);
        TreeNode assemblyItemNode = new TreeNode(assemblyItem);
        TreeNode nodeToGet = null;
        if (mapofTrees.containsKey(assemblyItemNode)) {
            nodeToGet = mapofTrees.keySet().stream().filter(node -> node.equals(assemblyItemNode))
                    .findFirst().get();
       } else if (listofAssemblies.contains(assemblyItemNode)) {
           nodeToGet = listofAssemblies.stream().filter(node -> node.equals(assemblyItemNode))
                   .findFirst().get();
       }
        return nodeToGet;
    }

    private void throwAssemblyExists() throws RuleBrokenException {
        throw new RuleBrokenException(StringList.ASSEMBLY_ALREADY_EXISTS.toString());
    }
    
    private void throwAssemblyDoesntExist(String name) throws RuleBrokenException {
        throw new RuleBrokenException( "no BOM or component exists in the system for the specified name: " + name);
    }
}
