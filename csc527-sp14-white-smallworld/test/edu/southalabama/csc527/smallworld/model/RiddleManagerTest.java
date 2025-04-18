package edu.southalabama.csc527.smallworld.model;

import junit.framework.TestCase;


public class RiddleManagerTest extends TestCase {

    public void testAddAndGet() {
        RiddleManager rm = new RiddleManager();   
        Riddle r = new Riddle("p","a","f","s");
        rm.addRiddle("Room", r);
        
        // case-insensitive lookup
        assertSame(r, rm.getRiddleFor("ROOM"));
        assertSame(r, rm.getRiddleFor("room"));
        assertNull(rm.getRiddleFor("Other"));
    }
}
