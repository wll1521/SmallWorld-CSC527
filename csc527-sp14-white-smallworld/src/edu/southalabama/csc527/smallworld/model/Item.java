package edu.southalabama.csc527.smallworld.model;

import java.util.HashMap;
import java.util.Map;

public class Item {

    private final String name;
    private final String article;
    private int takePoints;
    private int dropPoints;
    
    // location-specific rules (key = location name)
    private Map<String, ItemLocationRule> f_locationRules = new HashMap<>();

    /**
     * Constructs an Item object with its name, article, and point awards.
     * 
     * @param name the unique name of the item
     * @param article the article ("a", "the", etc)
     * @param takePoints points awarded when the item is picked up
     * @param dropPoints points awarded when the item is dropped
     */
    public Item(String name, String article, int takePoints, int dropPoints) {
        if (name == null || article == null) {
            throw new IllegalArgumentException("Name and article must not be null.");
        }
        this.name = name;
        this.article = article;
        this.takePoints = takePoints;
        this.dropPoints = dropPoints;
    }

    public String getName() {
        return name;
    }

    public String getArticle() {
        return article;
    }

    public String getShortDescription() {
        return article + " " + name;
    }

    public int getTakePoints() {
        return takePoints;
    }

    public int getDropPoints() {
        return dropPoints;
    }

    /**
     * Zero the take points after awarding to prevent abuse
     */
    public void zeroTakePoints() {
        takePoints = 0;
    }

    /**
     * Zero the drop points after awarding to prevent abuse
     */
    public void zeroDropPoints() {
        dropPoints = 0;
    }
    
    // Adds a specific rule for a given place
    public void addLocationRule(String locationName, ItemLocationRule rule) {
        f_locationRules.put(locationName.toUpperCase(), rule);
    }
    
    // Getter for rules within a specific location
    public ItemLocationRule getLocationRule(String locationName) {
        return f_locationRules.get(locationName.toUpperCase());
    }

    @Override
    public String toString() {
        return getShortDescription();
    }
}
