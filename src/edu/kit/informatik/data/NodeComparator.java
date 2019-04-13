package edu.kit.informatik.data;

import java.util.Comparator;

/**
 * Compares two nodes. It compares the amounts first. If they are equal, then the names are compared.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class NodeComparator implements Comparator<TreeNode> {
   @Override
    public int compare(TreeNode firstNode, TreeNode secondNode) {
        int intComparator = Integer.compare(firstNode.getAmountofData(), secondNode.getAmountofData());
        if (intComparator == 0) {
            return firstNode.getNameofData().compareTo(secondNode.getNameofData());
        }
        // To reverse the order (since this is what is required by the exercise).
        return (intComparator > 0) ? -1 : 1;
    }
}
