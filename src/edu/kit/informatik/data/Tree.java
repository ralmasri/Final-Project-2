package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a "super" assembly, as in an assembly that isn't a part of any
 * other assemblies.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class Tree {

    /** The root of the tree, which is also an assembly. */
    private TreeNode root;

    /**
     * Constructor for the tree.
     * 
     * @param data The data inside the node.
     */
    public Tree(Item data) {
        this.root = new TreeNode(data);
        root.setTreeName(root.getNameofData());
    }

    /**
     * Constructor for a tree with a root node that already exists.
     * 
     * @param root The root node.
     */
    public Tree(TreeNode root) {
        this.root = root.getCopy();
    }

    /**
     * Getter method for the root of a tree.
     * 
     * @return The root node.
     */
    public TreeNode getRootElement() {
        return root;
    }

    /**
     * Deletes all the children of a node.
     * 
     * @param parent The node of which the children will be deleted.
     */
    public void removeChildren(TreeNode parent) {
        parent.deleteChildren();
    }


    /**
     * Adds a child node under a parent.
     * 
     * @param child       The child node to be added.
     * @param parent      The parent node under which the child will be added.
     * @param doesChildExist True if the child already exists, otherwise false.
     */
    public void addNode(TreeNode child, TreeNode parent, boolean doesChildExist) {
        if (doesChildExist) {
            TreeNode temp = child.getCopy();
            parent.addChild(temp);
        } else {
            parent.addChild(child);
        }
    }
    
    /**
     * Delete a tree.
     */
    public void deleteTree() {
        this.root = null;
    }
    
    /**
     * Method to check for cycles when adding multiple children.
     * @param assembly The assembly we want to add said children to.
     * @param children The children.
     * @return Custom error message if a cycle would occur, otherwise empty string.
     */
    public static String checkForCycles(TreeNode assembly, List<TreeNode> children) {
        List<TreeNode> ancestors = new ArrayList<>();
        List<TreeNode> descendants = new ArrayList<>();
        for (TreeNode child : children) {
            if (assembly.getNameofData().equals(child.getNameofData())) {
                return child.getNameofData() + "-" + child.getNameofData();
            }
            ancestors = assembly.getAncestors();
            descendants.add(child);
            descendants.addAll(child.getChildren());
            for (TreeNode descendant : descendants) {
                for (TreeNode ancestor : ancestors) {
                    if (descendant.getNameofData().equals(ancestor.getNameofData())) {
                        return createErrorMessage(descendant, ancestor, ancestors, assembly);
                    }
                }
            }
        }
        return "";
    }

    /**
     * Method to create a custom message in case a cycle is formed. 
     * @param descendantCycle The descendant node that would cause a cycle.
     * @param ancestorCycle The ancestor node that would cause a cycle.
     * @param ancestors All ancestors of the assembly.
     * @param assembly The assembly.
     * @return The custom error message.
     */
    private static String createErrorMessage(TreeNode descendantCycle, TreeNode ancestorCycle, List<TreeNode> ancestors,
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
     * Method to find all nodes in a tree of a certain name.
     * 
     * @param node The node we are checking
     * @param name The name of the node(s) we want.
     * @param nodes The list of found nodes.
     * @return A list of found nodes.
     */
    public List<TreeNode> findNodes(TreeNode node, String name, List<TreeNode> nodes) {
        for (TreeNode child : node.getChildren()) {
            if (child.getNameofData().equals(name)) {
                nodes.add(child);
            }
            findNodes(child, name, nodes);
        }
        return nodes;
    }

    /**
     * Getter method for the name of the root of a tree.
     * @return The name of the root.
     */
    public String getRootName() {
        return root.getNameofData();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass().equals(obj.getClass())) {
            Tree otherTree = (Tree) obj;
            return this.root.equals(otherTree.root);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(root);
    }
}
