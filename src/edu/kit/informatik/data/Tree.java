package edu.kit.informatik.data;

import java.util.List;
import java.util.Objects;

import edu.kit.informatik.exceptions.RuleBrokenException;

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
     * @param doesItExist True if the tree node already exists, otherwise false.
     * @throws RuleBrokenException When the addition of said child node creates a
     *                             cycle in the tree.
     */
    public void addNode(TreeNode child, TreeNode parent, boolean doesChildExist) {
        // TODO Make sure node you are using this method on is NOT a rootNode and remove
        // its tree from the system.
        assert !child.isRoot();
        if (doesChildExist) {
            TreeNode temp = child.getCopy();
            parent.addChild(temp);
        } else {
            parent.addChild(child);
        }
    }
    
    public void deleteTree() {
        this.root = null;
    }

    

    /**
     * 
     * @param parent
     * @param child
     * @return
     */
    public String getCycleErrorMessage(TreeNode parent, TreeNode child) {
        if (child.equals(parent)) { // Will always create a cycle.
            return parent.getNameofData() + "-" + child.getNameofData();
        }
        // If the node to be added has no children (a.k.a a part) then it cannot create
        // a cycle.
        if (child.getChildren().isEmpty()) {
            return "";
        }
        StringBuilder errormsgBuilder = new StringBuilder();
        String errormsg = getCyclicErrorMessage(parent, child, errormsgBuilder);
        if (errormsg == null) {
            return "null";
        }
        return new StringBuilder(errormsg).reverse().append("-").append(child.getNameofData()).toString();
    }

    private String getCyclicErrorMessage(TreeNode parent, TreeNode child, StringBuilder errormsg) {
        if (parent.equals(root)) {
            return "";
        } else if (parent.equals(child)) {
            return errormsg.append(child.getNameofData()).toString();
        } else {
            errormsg.append(parent.getNameofData()).append("-");
            getCyclicErrorMessage(parent.getParent(), child, errormsg);
        }
        return errormsg.toString();
    }

    /**
     * Method to find a specific node.
     * 
     * @param node The node we are checking
     * @param name The name of the node we want.
     * @return The node if it is found, otherwise null.
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
