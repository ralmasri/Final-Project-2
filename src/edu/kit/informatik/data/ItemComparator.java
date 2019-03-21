package edu.kit.informatik.data;

import java.util.Comparator;

/**
 * Compares two items by the name based on Unicode values.
 * 
 * @author Rakan Zeid Al Masri
 * @version 1.0
 */

public class ItemComparator implements Comparator<Item> {

    @Override
    public int compare(Item firstItem, Item secondItem) {
        return firstItem.getName().compareTo(secondItem.getName());
    }


}
