package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.List;
import edu.kit.informatik.exceptions.RuleBrokenException;
import edu.kit.informatik.util.StringList;

/**
 * Represents a "super" assembly, as in an assembly that isn't a part of any other assemblies.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class Tree {
    
    /** The root of the tree, which is also an assembly. */
    private TreeNode root;
    
    /** A list of all nodes under the root */
    private List<TreeNode> allNodes;
    
    /**
     * Constructor for the tree.
     * @param data The data inside the node.
     */
    public Tree(Item data) {
       this.root = new TreeNode(data);
       this.allNodes = new ArrayList<>();
       allNodes.add(this.root);
    }
    
    /**
     * Constructor for a tree with a root node that already exists.
     * @param root The root node.   
     */
    public Tree(TreeNode root) {
        this.root = root;
        this.allNodes = new ArrayList<>();
        allNodes.add(this.root);
    }
    
    /**
     * Getter method for the root of a tree.
     * @return The root node.
     */
    public TreeNode getRootElement() {
        return root;
    }
    
    /**
     * Deletes all the children of a node.
     * @param parent The node of which the children will be deleted.
     */
    public void removeChildren(TreeNode parent) {
        for (TreeNode child : parent.getChildren()) {
            allNodes.remove(child);
        }
        parent.deleteChildren();
    }
    
    private List<TreeNode> getAncestors(TreeNode child) {
        List<TreeNode> ancestors = new ArrayList<>();
        for (TreeNode parent = child.getParent(); parent != null; parent = parent.getParent()) {
            ancestors.add(parent);
        }
        return ancestors;
    }
    
    /**
     * This is when the addition is to the root.
     * @param child The child node to be added.
     */
    public void addNode(TreeNode child) {
        root.addChild(child);
    }
    
    /**
     * Adds a child node under a parent.
     * 
     * @param child The child node to be added.
     * @param parent The parent node under which the child will be added.
     * @throws RuleBrokenException When the addition of said child node creates a cycle in the tree.
     */
    public void addNode(TreeNode child, TreeNode parent) throws RuleBrokenException {
        /*TreeNode child = parent.getChildren()
                .stream().filter(node -> node.getNameofData().equals(name))
                .findFirst().orElse(new TreeNode(new Item(name)));*/
        // TODO Make sure node you are using this method on is NOT a rootNode and remove its tree from the system.
        assert !child.isRoot();
        String errorMsg = getCycleErrorMessage(parent, child);
        if (!errorMsg.isEmpty()) {
            throw new RuleBrokenException(StringList.CYCLE_ERROR_MSG.toString() + errorMsg);
        }
        parent.getChildren().add(child);
        allNodes.add(child);
    }
    
    /**
     * Method to add a tree to an already existing tree.
     * @param tree The tree to be added.
     * @param parent The parent of the tree's root element.
     * @throws RuleBrokenException If a cycle is found because of this addition.
     */
    public void addTree(Tree tree, TreeNode parent) throws RuleBrokenException {
        List<TreeNode> ancestors = getAncestors(parent);
        ancestors.add(parent);
        TreeNode rootOfNewTree = tree.getRootElement();
        for (TreeNode node : tree.allNodes) {
            for (TreeNode ancestor : ancestors) {
                if (node.equals(ancestor)) {
                    throw new RuleBrokenException(StringList.CYCLE_ERROR_MSG.toString() 
                            + generateTreeErrorMsg(node, parent, rootOfNewTree));
                }
            }
        }
        parent.getChildren().add(rootOfNewTree);
        allNodes.add(rootOfNewTree);
    }
    
    private String generateTreeErrorMsg(TreeNode node, TreeNode parent, TreeNode rootOfNewTree) {
        Tree tempTree = this;
        TreeNode tempParent = null;
                for (TreeNode treeNode : tempTree.allNodes) {
                    if (treeNode.equals(parent)) {
                        tempParent = treeNode;
                        break;
                    }
                }
                tempParent.addChild(rootOfNewTree);
                return getCycleErrorMessage(node.getParent(), node);
    }
    
   /* private List<TreeNode> getDescendents(TreeNode node, List<TreeNode> descendents) {
        for (TreeNode child : node.getChildren()) {
        }
    }*/
    
    private String getCycleErrorMessage(TreeNode parent, TreeNode child) {
        List<TreeNode> visitedNodes = new ArrayList<>();
        if (child.equals(parent)) { // Will always create a cycle.
            return parent.getNameofData() + "-" + child.getNameofData();
        }
        // If the node to be added has no children (a.k.a a part) then it cannot create a cycle.
        if (child.getChildren().isEmpty()) {
            return "";
        }
        String errormsg = getCyclicErrorMessage(visitedNodes, parent, child);
        return new StringBuilder(errormsg).reverse().append("-").append(child.getNameofData()).toString();
    }
    
    /*private boolean traverse(List<TreeNode> visitedNodes, TreeNode source) {
        visitedNodes.add(source);
        for (TreeNode node : source.getChildren()) {
            if (visitedNodes.contains(node) && !node.isLeaf()) {
                return true;
            } else {
                visitedNodes.add(node);
                traverse(visitedNodes, node);
            }
        }
        return false;
    }*/
    
    
   private String getCyclicErrorMessage(List<TreeNode> visitedNodes, TreeNode parent, TreeNode child) {
       visitedNodes.add(parent);
       StringBuilder errormsg = new StringBuilder();
       if (parent.equals(root)) {
           return "";
       } else if (visitedNodes.contains(child)) {
           return errormsg.append(child.getNameofData()).toString();
       } else { 
           errormsg.append(parent.getNameofData()).append("-");
           getCyclicErrorMessage(visitedNodes, parent.getParent(), child);
       }
    return null;
   }
   
   /**
    * Getter method for all the nodes of a tree.
    * @return The list of all nodes in a tree.
    */
   public List<TreeNode> getAllNodes() {
       return allNodes;
   }
   
   public TreeNode findNode(TreeNode node, String name) {
       if (node.getNameofData().equals(name)) {
           return node;
       } else {
           for (TreeNode child : node.getChildren()) {
               TreeNode result = findNode(child, name);
               if (result != null) {
                   return result;
               }
           }
       }
       return null;
   }
}
