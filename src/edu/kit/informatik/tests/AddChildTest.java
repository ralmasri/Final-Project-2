package edu.kit.informatik.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.kit.informatik.data.*;
import edu.kit.informatik.exceptions.RuleBrokenException;
/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class AddChildTest {

    @Test
    public void testAddChildren() throws RuleBrokenException {
        TreeNode a = new TreeNode(new Item(5, "A"));
        TreeNode b = new TreeNode(new Item(2, "B"));
        TreeNode c = new TreeNode(new Item(2, "C"));
        TreeNode d = new TreeNode(new Item(2, "D"));
        TreeNode e = new TreeNode(new Item(2, "E"));
        TreeNode f = new TreeNode(new Item(2, "F"));
        TreeNode g = new TreeNode(new Item(2, "G"));
        TreeNode h = new TreeNode(new Item(2, "H"));
        TreeNode i = new TreeNode(new Item(2, "I"));
        NeedsAssessment lads = new NeedsAssessment();
        List<TreeNode> children = new ArrayList<>();
        List<TreeNode> childrentwo = new ArrayList<>();
        List<TreeNode> childrenthree = new ArrayList<>();
        List<TreeNode> childrenfour = new ArrayList<>();
        List<String> experiment = new ArrayList<>();
        children.add(b);
        children.add(c);
        TreeNode bCopy = new TreeNode(new Item(2, "B"));
        TreeNode eCopy = new TreeNode(new Item(2, "E"));
        childrentwo.add(e);
        childrenthree.add(bCopy);
        childrenfour.add(eCopy);
        try {
            lads.addAssembly("A", children);
            lads.addAssembly("C", childrentwo);
            lads.addAssembly("E", childrenthree);
            lads.removeAssembly("E");
            lads.addAssembly("B", childrenfour);
            System.out.println(lads.getAssemblies("A"));
            System.out.println(lads.printAssembly("A"));
            System.out.println(lads.getComponents("A"));
        } catch (RuleBrokenException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
