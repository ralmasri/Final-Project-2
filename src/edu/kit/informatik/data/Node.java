package edu.kit.informatik.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class Node {

    private Item data;
    private List<Node> children;
    
    public Node() {
        super();
    }
    
    public Node(Item data) {
        this();
        setData(data);
    }
    
    public List<Node> getChildren(){
        if (this.children == null) {
            return new ArrayList<>();
        }
        
        return this.children;
    }
    
    public boolean isLeaf() {
        return this.children.size() == 0;
    }
    
    public void setChildren(List<Node> children) {
        this.children = children;
    }
    
    public int getNumberofChildren() {
        if (children == null) {
            return 0;
        }
        
        return children.size();
    }
    
    public void addChild(Node child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }
    
    public void insertChildAt(int index, Node child) {
        if (index == getNumberofChildren()) {
            addChild(child);
            return;
        } else {
            children.get(index);
            children.add(index, child);
        }
    }
    
    public void removeChildAt(int index) {
        children.remove(index);
    }
    
    public Item getData() {
        return this.data;
    }
    
    public void setData(Item data) {
        this.data = data;
    }
}
