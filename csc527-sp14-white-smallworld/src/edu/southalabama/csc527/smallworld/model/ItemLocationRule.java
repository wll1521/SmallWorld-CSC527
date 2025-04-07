package edu.southalabama.csc527.smallworld.model;

public class ItemLocationRule {
    private boolean neededToEnter;	// indicates if item is required to enter
    private String blockedMsg;		// message shown if blocked
    private int takePoints;			
    private int dropPoints;
    
    public ItemLocationRule(boolean neededToEnter, String blockedMsg, int takePoints, int dropPoints) {
        this.neededToEnter = neededToEnter;
        this.blockedMsg = blockedMsg;
        this.takePoints = takePoints;
        this.dropPoints = dropPoints;
    }
    
    public boolean isNeededToEnter() {
        return neededToEnter;
    }
    
    public String getBlockedMsg() {
        return blockedMsg;
    }
    
    public int getTakePoints() {
        return takePoints;
    }
    
    public int getDropPoints() {
        return dropPoints;
    }
}
