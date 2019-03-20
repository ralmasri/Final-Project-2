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

    public List<TreeNode> getAncestors() {
        return getAncestorList(new ArrayList<>(), this);
    }

    private List<TreeNode> getAncestorList(List<TreeNode> ancestors, TreeNode node) {
        if (node.getParent() != null) {
            ancestors.add(node.getParent());
            getAncestorList(ancestors, node.getParent());
        }
        return ancestors;
    }

    public List<TreeNode> getDescendants() {
        return getListDescendants(this, new HashMap<>());
    }

    public List<TreeNode> getListDescendants(TreeNode node, Map<String, TreeNode> listofChildren) {
        for (TreeNode child : node.getChildren()) {
            if (!listofChildren.containsKey(child.getNameofData())) {
                List<TreeNode> ancestors = child.getAncestors();
                int multiplier = 1;
                for (TreeNode ancestor : ancestors) {
                    multiplier *= ancestor.getAmountofData();
                }
                TreeNode childCopy = child.getCopy();
                childCopy.getData().setAmount(childCopy.getAmountofData() * multiplier);
                listofChildren.put(childCopy.getNameofData(), childCopy);
            } else {
                List<TreeNode> ancestors = child.getAncestors();
                int multiplier = 1;
                for (TreeNode ancestor : ancestors) {
                    multiplier *= ancestor.getAmountofData();
                }
                int multipliedAmount = multiplier * child.getAmountofData();
                int oldAmount = listofChildren.get(child.getNameofData()).getAmountofData();
                int newAmount = oldAmount + multipliedAmount;
                listofChildren.get(child.getNameofData()).getData().setAmount(newAmount);
            }
            getListDescendants(child, listofChildren);
        }
        return new ArrayList<>(listofChildren.values());
    }

   

    /**
     * Setter method for tree.
     * 
     * @param tree The tree that the node is in.
     */
    public void setTreeName(String treeRootName) {
        this.treeRootName = treeRootName;
    }

    public String getTreeName() {
        return treeRootName;
    }

    public void setData(Item data) {
        this.data = data;
    }

    public TreeNode getCopy() {
        return copyNode(this, new HashMap<>(), treeRootName);
    }

    private TreeNode copyNode(TreeNode root, Map<TreeNode, TreeNode> images, String treeName) {
        TreeNode copy = images.get(root);
        if (copy == null) {
            copy = new TreeNode(new Item(root.getAmountofData(), root.getNameofData()));
            copy.setTreeName(treeName);
            images.put(root, copy);
            for (TreeNode child : root.getChildren()) {
                copy.addChild(copyNode(child, images, treeName));
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
            return this.data.equals(otherTreeNode.data) && this.treeRootName.equals(otherTreeNode.treeRootName)
                    && this.parent.getNameofData().equals(otherTreeNode.getNameofData());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, treeRootName, parent.getNameofData());
    }

    @Override
    public String toString() {
        return this.data.toString();
    }
}