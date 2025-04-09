package edu.southalabama.csc527.smallworld.model;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    /**
     * Attempts to take an item from the current place and add it to the player's inventory.
     * Awards take points if it exists.
     * 
     * @param player the player taking the item
     * @param currentPlace the place where the item is located
     * @param item the item to take
     * @return true if the item was successfully taken, false otherwise
     */
    public static boolean takeItem(Player player, Place currentPlace, Item item) {
        // Verify that the item is in the current place
        if (!currentPlace.getItems().contains(item)) {
            player.getWorld().addToMessage("Item " + item.getShortDescription() + " is not here.");
            return false;
        }
        // Check if the player already has the item
        if (player.getInventory().contains(item)) {
            player.getWorld().addToMessage("You are already carrying " + item.getShortDescription() + ".");
            return false;
        }
        // Transfer the item from the place to the player's inventory
        currentPlace.getItems().remove(item);
        player.getInventory().add(item);
        // Award take points and zero them
        int points = item.getTakePoints();
        player.addPoints(points);
        item.zeroTakePoints();
        player.getWorld().addToMessage("You have taken " + item.getShortDescription() + " for " + points + " points.");
        return true;
    }

    /**
     * Takes all items from the current place.
     * 
     * @param player the player taking items
     * @param currentPlace the place containing items
     */
    public static void takeAll(Player player, Place currentPlace) {
        List<Item> itemsToTake = new ArrayList<>(currentPlace.getItems());
        for (Item item : itemsToTake) {
            takeItem(player, currentPlace, item);
        }
    }

    /**
     * Drops an item from the player's inventory into the current place.
     * Awards drop points / location-specific drop points if they exist.
     * 
     * @param player the player dropping the item
     * @param currentPlace the place where the item is dropped
     * @param item the item to drop
     * @return true if the item was dropped, false otherwise
     */
    public static boolean dropItem(Player player, Place currentPlace, Item item) {
        // Verify the player is carrying the item
        if (!player.getInventory().contains(item)) {
            player.getWorld().addToMessage("You are not carrying " + item.getShortDescription() + ".");
            return false;
        }
        
        // Check if the item is required in the current location 
        // Fix for stuck player
        ItemLocationRule rule = item.getLocationRule(currentPlace.getName());
        if (rule != null && rule.isNeededToEnter()) {
            player.getWorld().addToMessage("You cannot drop " + item.getShortDescription() + " because it is required to enter this location.");
            return false;
        }
        
        // Remove the item from inventory and add it to the current place
        player.getInventory().remove(item);
        currentPlace.getItems().add(item);
        
        // Award the base drop points and zero them
        int points = item.getDropPoints();
        player.addPoints(points);
        item.zeroDropPoints();
        
        // Check for a location-specific drop bonus
        if (rule != null) {
            int bonus = rule.getDropPoints();
            if (bonus != 0) {
                player.addPoints(bonus);
                player.getWorld().addToMessage("Additional bonus " + bonus 
                    + " points for dropping " + item.getShortDescription() 
                    + " at " + currentPlace.getName() + ".");
            }
        }
        player.getWorld().addToMessage("You have dropped " + item.getShortDescription() 
            + " for " + points + " points.");
        return true;
    }


    /**
     * Returns an inventory list.
     * 
     * @param player the player whose inventory is to be listed
     * @return a string describing the inventory
     */
    public static String listInventory(Player player) {
        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            return "You are not holding any items.";
        }
        StringBuilder sb = new StringBuilder("You are carrying:\n");
        for (Item item : inventory) {
            sb.append("- ").append(item.getShortDescription()).append("\n");
        }
        return sb.toString();
    }
}
