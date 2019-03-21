package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a node. A node can either be an assembly or a part.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class TreeNode {

    /** The name of the tree that this tree node is in. */
    private String treeRootName;

    /** The data stored in the node. */
    private Item data;

    /** A list of children of a node. */
    private List<TreeNode> children;

    /** The parent of a node. */
    private TreeNode parent;

    /**
     * Constructor for a tree node.
     * 
     * @param data The data stored in the tree node.
     */
    public TreeNode(Item data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    /**
     * Getter method for the data inside a node.
     * 
     * @return The data inside the node.
     */
    public Item getData() {
        return this.data;
    }

    /**
     * Getter method for the children of a node.
     * 
     * @return The children of the node.
     */
    public List<TreeNode> getChildren() {
        return this.children;
    }

    /**
     * Checks whether a node is a leaf or not (a.k.a if the node is a component).
     * 
     * @return true if the node is a leaf, otherwise false.
     */
    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    /**
     * Adds a child to a node.
     * 
     * @param child The child to be added.
     */
    public void addChild(TreeNode child) {
        child.setParent(this);
        child.setTreeName(this.treeRootName);
        children.add(child);
    }

    /**
     * Getter method for the parent of a node.
     * 
     * @return The parent of the node.
     */
    public TreeNode getParent() {
        return this.parent;
    }

    /**
     * Setter method for the parent node.
     * @param parent The parent node.
     */
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * Getter method for the name of the item inside a node.
     * 
     * @return The name of the item inside the node.
     */
    public String getNameofData() {
        return this.data.getName();
    }

    /**
     * Getter method for the amount of the item inside a node.
     * 
     * @return The amount of the item inside the node.
     */
    public int getAmountofData() {
        return this.data.getAmount();
    }

    /**
     * Deletes a node's children.
     */
    public void deleteChildren() {
        this.children = new ArrayList<>();
    }

    /**
     * Method to check whether a node is a root or not.
     * 
     * @return true if the node is a root, otherwise false.
     */
    public boolean isRoot() {
        return !this.children.isEmpty() && this.parent == null;
    }

    /**
     * Method to check whether all the children of a node are components(leaves).
     * 
     * @return true if all the children are leaves, false if one or more are
     *         assemblies.
     */
    public boolean areChildrenComponents() {
        for (TreeNode child : this.getChildren()) {
            if (!child.isLeaf()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to get the ancestors of a node (all nodes above a given node).
     * @return The list of ancestors.
     */
    public List<TreeNode> getAncestors() {
        return getAncestorList(new ArrayList<>(), this);
    }

    /**
     * Recursive method to get the ancestors.
     * @param ancestors The current list of ancestors.
     * @param node The current node.
     * @return The list of ancestors.
     */
    private List<TreeNode> getAncestorList(List<TreeNode> ancestors, TreeNode node) {
        if (node.getParent() != null) {
            ancestors.add(node.getParent());
            getAncestorList(ancestors, node.getParent());
        }
        return ancestors;
    }

    /**
     * Method to get the descendants of a node (all nodes under a given node).
     * @return The list of descendants.
     */
    public List<TreeNode> getDescendants() {
        return getListDescendants(this, new HashMap<>(), this.getNameofData());
    }

    /**
     * Recursive method to get the descendants of a node.
     * 
     * This method also takes into account the multiplier and returns the list of descendants
     * with their proper amounts.
     * 
     * @param node The current node.
     * @param listofChildren The list of descendants.
     * @param originalName The name of the original node we are checking.
     * @return The list of descendants.
     */
    private List<TreeNode> getListDescendants(TreeNode node, Map<String, TreeNode> listofChildren, 
            String originalName) {
        for (TreeNode child : node.getChildren()) {
            // If the node wasn't already added.
            if (!listofChildren.containsKey(child.getNameofData())) {
                List<TreeNode> ancestors = child.getAncestors();
                int multiplier = 1; // The amounts of all ancestors multiplied
                for (TreeNode ancestor : ancestors) {
                    if (ancestor.getNameofData().equals(originalName)) {
                        break;
                    }
                    multiplier *= ancestor.getAmountofData();
                }
                TreeNode childCopy = child.getCopy();
                childCopy.getData().setAmount(child.getAmountofData() * multiplier);
                listofChildren.put(childCopy.getNameofData(), childCopy);
            } else { // If it was added then we just add the amount to the one in the list.
                List<TreeNode> ancestors = child.getAncestors();
                int multiplier = 1; // The amounts of all ancestors multiplied
                for (TreeNode ancestor : ancestors) {
                    if (ancestor.getNameofData().equals(originalName)) {
                        break;
                    }
                    multiplier *= ancestor.getAmountofData();
                }
                int multipliedAmount = multiplier * child.getAmountofData();
                int oldAmount = listofChildren.get(child.getNameofData()).getAmountofData();
                int newAmount = oldAmount + multipliedAmount;
                listofChildren.get(child.getNameofData()).getData().setAmount(newAmount);
            }
            getListDescendants(child, listofChildren, originalName);
        }
        return new ArrayList<>(listofChildren.values());
    }

   

    /**
     * Setter method for tree name.
     * 
     * @param treeRootName The name of the tree to which said tree node will be added to.
     */
    public void setTreeName(String treeRootName) {
        this.treeRootName = treeRootName;
    }

    /**
     * Getter method for the tree name.
     * @return The tree name.
     */
    public String getTreeName() {
        return treeRootName;
    }

    /**
     * Method to clone a node by value and not by reference.
     * @return The cloned node.
     */
    public TreeNode getCopy() {
        return copyNode(this, new HashMap<>(), treeRootName);
    }

    /**
     * Recursive method to completely clone a node and its children
     * @param node The current node.
     * @param map Map of copied nodes and their copies.
     * @param treeName The name of the tree of the original node.
     * @return The cloned node.
     */
    private TreeNode copyNode(TreeNode node, Map<TreeNode, TreeNode> map, String treeName) {
        TreeNode copy = map.get(node);
        if (copy == null) {
            copy = new TreeNode(new Item(node.getAmountofData(), node.getNameofData()));
            copy.setTreeName(treeName);
            map.put(node, copy);
            for (TreeNode child : node.getChildren()) {
                copy.addChild(copyNode(child, map, treeName));
            }
        }
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass().equals(obj.getClass())) {
            TreeNode otherTreeNode = (TreeNode) obj;
            return this.data.equals(otherTreeNode.data) && this.treeRootName.equals(otherTreeNode.treeRootName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, treeRootName);
    }

    @Override
    public String toString() {
        return this.data.toString();
    }
}