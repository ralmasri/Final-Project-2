package edu.kit.informatik.data;

import java.util.Comparator;

/**
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
        return (intComparator > 0) ? -1 : 1;
    }

}
