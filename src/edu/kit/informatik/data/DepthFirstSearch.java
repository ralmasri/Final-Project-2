package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class DepthFirstSearch {
    
    public void hasCycle (TreeNode root) {
        List<TreeNode> visited = new ArrayList<>();
        dfs(root, visited);
    }
    private boolean dfs(TreeNode root, List<TreeNode> visited) {
        if (visited.contains(root)) {
            return true;
        }
        visited.add(root);
        for (TreeNode node : root.getChildren()) {
            if (dfs(node, visited)) {
                return true;
            }
        }
    }
}
