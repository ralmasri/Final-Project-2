package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents the system (storage of all parts/assemblies).
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class SystemInitializer {

    /** Maximum amount of a part or an assembly. */
    private static final int MAX_AMOUNT = 1000;
    
    /** List of assemblies that are a part of a tree. */
    private List<TreeNode> listofAssemblies;

    /** Keeps track of all parts. */
    private List<TreeNode> listofParts;
    
    /**
     * A list of all trees (assemblies that are not a part of anything). Keys are
     * the roots of the trees.
     */
    private Map<String, Tree> mapofTrees;
    
    /**
     * Constructor for the system initializer.
     */
    public SystemInitializer() {
        this.mapofTrees = new HashMap<>();
        this.listofAssemblies = new ArrayList<>();
        this.listofParts = new ArrayList<>();
    }

    /**
     * @return the mapofTrees
     */
    public Map<String, Tree> getMapofTrees() {
        return mapofTrees;
    }

    /**
     * @return the listofAssemblies
     */
    public List<TreeNode> getListofAssemblies() {
        return listofAssemblies;
    }

    /**
     * @return the listofParts
     */
    public List<TreeNode> getListofParts() {
        return listofParts;
    }

    /**
     * @return The maximum amount allowed for an item.
     */
    public static int getMaxAmount() {
        return MAX_AMOUNT;
    }
    
    /**
     * Method to get all assemblies of a certain name in the system.
     * @param nameofAssembly The name of the assembly.
     * @return A list of all instances of said assembly.
     */
    List<TreeNode> getAllAssembliesOfName(String nameofAssembly) {
        List<TreeNode> nodes = new ArrayList<>();
        if (mapofTrees.containsKey(nameofAssembly)) {
            nodes.add(mapofTrees.get(nameofAssembly).getRootElement());
        } else {
            for (TreeNode node : listofAssemblies) {
                if (node.getNameofData().equals(nameofAssembly)) {
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }
    
    /**
     * Method to get an assembly of a certain name from the system.
     * @param nameofNode The name of said assembly.
     * @return The assembly if found, otherwise null.
     */
    public TreeNode getAssembly(String nameofNode) {
        if (mapofTrees.containsKey(nameofNode)) {
            return mapofTrees.get(nameofNode).getRootElement();
        }
        for (TreeNode assembly : listofAssemblies) {
            if (assembly.getNameofData().equals(nameofNode)) {
                return assembly;
            }
        }
        return null;
    }

    /**
     * Method to get a part of a certain name from the system.
     * @param nameofPart The name of said part.
     * @return The part if found, otherwise null.
     */
    TreeNode getPart(String nameofPart) {
        for (TreeNode part : listofParts) {
            if (part.getNameofData().equals(nameofPart)) {
                return part;
            }
        }
        return null;
    }
}
