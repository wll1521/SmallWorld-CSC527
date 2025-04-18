package edu.southalabama.csc527.smallworld.model;

import java.util.HashMap;
import java.util.Map;

// keeps lookup for each place in reference to its riddle
public class RiddleManager {
    private final Map<String, Riddle> riddles = new HashMap<>();

    public void addRiddle(String placeName, Riddle riddle) {
        riddles.put(placeName.toUpperCase(), riddle);
    }

    public Riddle getRiddleFor(String placeName) {
        return riddles.get(placeName.toUpperCase());
    }
}
