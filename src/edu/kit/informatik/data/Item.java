package edu.kit.informatik.data;

import java.util.Objects;

/**
 * A class that represents a general abstraction of a component and an assembly.
 * 
 * The only difference between a component and an assembly is that an assembly has children
 * which is why this abstraction is used.
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class Item {

    /** The name of the item. */
    private String name;
    
    /** The amount of the item. */
    private int amount;
    
    /**
     * Constructor for an item.
     * @param amount The amount of an item.
     * @param name The name of an item.
     */
    public Item(int amount, String name) {
        this.name = name;
        this.amount = amount;
    }
    
    /**
     * Constructor for an item when it is newly created, which means there is only one of it.
     * @param name The name of the item.
     */
    public Item(String name) {
        this.name = name;
        this.amount = 1;
    }
    
    /**
     * Setter method for the amount.
     * @param amount The new amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    /**
     * Getter method for the name of an item.
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Getter method for the amount of an item.
     * @return The amount of an item.
     */
    public int getAmount() {
        return amount;
    }
    
    @Override
    public String toString() {
        return name + ":" + String.valueOf(amount);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass().equals(obj.getClass())) {
            Item otherItem = (Item) obj;
            return this.name.equals(otherItem.name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
