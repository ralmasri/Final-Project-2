package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Represents a node. A node can either be an assembly or a part.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class TreeNode {

    /** The data stored in the node. */
    private Item data;
    
    /** A list of children of a node. */
    private List<TreeNode> children;
    
    /** The parent of a node. */
    private TreeNode parent;
    
    /**
     * Constructor for a tree node.
     * @param data The data stored in the tree node.
     */
    public TreeNode(Item data) {
        this.data = data;
        this.children = new ArrayList<>();
    }
    
    /**
     * Getter method for the data inside a node.
     * @return The data inside the node.
     */
    public Item getData() {
        return this.data;
    }
    
    /**
     * Getter method for the children of a node.
     * @return The children of the node.
     */
    public List<TreeNode> getChildren() {
        return this.children;
    }
    
    /**
     * Checks whether a node is a leaf or not (a.k.a if the node is a component).
     * @return true if the node is a leaf, otherwise false.
     */
    public boolean isLeaf() {
        return this.children.isEmpty();
    }
    
    /**
     * Adds a child to a node.
     * @param child The child to be added.
     */
    public void addChild(TreeNode child) {
       if (!children.contains(child)) {
           children.add(child);
           child.parent = this;
       }
    }
    
    /**
     * Getter method for the parent of a node.
     * @return The parent of the node.
     */
    public TreeNode getParent() {
        return this.parent;
    }
    
    /**
     * Getter method for the name of the item inside a node.
     * @return The name of the item inside the node.
     */
    public String getNameofData() {
        return this.data.getName();
    }
    
    /**
     * Getter method for the amount of the item inside a node.
     * @return The amount of the item inside the node.
     */
    public int getAmountofData() {
        return this.data.getAmount();
    }
    
    /**
     * Deletes a node's children.
     */
    public void deleteChildren() {
        this.children = null;
    }
    
    /**
     * Method to check whether a node is a root or not.
     * @return true if the node is a root, otherwise false.
     */
    public boolean isRoot() {
        return this.children != null && this.parent == null;
    }
    
    /**
     * Method to check whether all the children of a node are components(leaves).
     * @return true if all the children are leaves, false if one or more are assemblies.
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
     * Method to get all descendants of a node that are also assemblies.
     * @return The list of descendants that are assemblies.
     */
    public List<TreeNode> getDescendantAssemblies() {
        return getListofDescendants(this, new ArrayList<>());
    }
    
    private List<TreeNode> getListofDescendants(TreeNode node, List<TreeNode> listofChildren) {
        for (TreeNode child : node.getChildren()) {
            if (!child.isLeaf()) {
                if (listofChildren.contains(child)) {
                    int index = listofChildren.indexOf(child);
                    TreeNode copyChild = listofChildren.get(index);
                    listofChildren.remove(index);
                    int newAmount = child.getAmountofData() + copyChild.getAmountofData();
                    Item largerItem = new Item(newAmount, child.getNameofData());
                    listofChildren.add(new TreeNode(largerItem));
                } else {
                    listofChildren.add(child);
                }
                getListofDescendants(child, listofChildren);
            }
        }
        return listofChildren;
    }
    
    /**
     * Method to get all descendants of a node that are components.
     * @return The list of descendants that are components.
     */
    public List<TreeNode> getDescendantComponents() {
        return getListofPartDescendants(this, new ArrayList<>());
    }
    
    private List<TreeNode> getListofPartDescendants(TreeNode node, List<TreeNode> listofChildren) {
        for (TreeNode child : node.getChildren()) {
            if (child.isLeaf()) {
                if (listofChildren.contains(child)) {
                    int index = listofChildren.indexOf(child);
                    TreeNode copyChild = listofChildren.get(index);
                    listofChildren.remove(index);
                    int newAmount = child.getAmountofData() + copyChild.getAmountofData();
                    Item largerItem = new Item(newAmount, child.getNameofData());
                    listofChildren.add(new TreeNode(largerItem));
                } else {
                    listofChildren.add(child);
                }
                getListofDescendants(child, listofChildren);
            }
        }
        return listofChildren;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass().equals(obj.getClass())) {
            TreeNode otherTreeNode = (TreeNode) obj;
            return this.data.equals(otherTreeNode.data);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
    
    @Override
    public String toString() {
        return this.data.toString();
    }
}