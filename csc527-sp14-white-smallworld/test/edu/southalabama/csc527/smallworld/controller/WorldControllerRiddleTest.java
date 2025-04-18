package edu.southalabama.csc527.smallworld.controller;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.TestConstants;
import edu.southalabama.csc527.smallworld.model.*;
import edu.southalabama.csc527.smallworld.model.Direction;


 //Test blocking travel & riddle‐guessing via WorldController.
 
public class WorldControllerRiddleTest extends TestCase {

    private WorldController wc;
    private World world;
    private String lastMessage; // Capture the last message sent during update

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        wc = new WorldController(TestConstants.TEST_RIDDLE_FILE);
        world = wc.getWorld();

        lastMessage = null;
        // Observer that saves the world's message before it's cleared
        world.addObserver(new IWorldObserver() {
            @Override
            public void update(World w) {
                lastMessage = w.getMessage();
            }
        });
    }

    public void testTravelBlockedByRiddle() {
        Player p     = world.getPlayer();
        Place start  = world.getPlace("Start");

        // Attempt to travel east (should be blocked)
        wc.travel(Direction.EAST);

        // Player should still be at start
        assertEquals(start, p.getLocation());

        // Should have gotten the riddle prompt
        assertNotNull(lastMessage);
        assertTrue(lastMessage.contains("What opens locks?"));
    }

    public void testAttemptRiddleWrongThenRight() {
        Player p      = world.getPlayer();
        Place start   = world.getPlace("Start");
        Place puzzle  = world.getPlace("PuzzleRoom");

        // 1) Block the initial travel
        wc.travel(Direction.EAST);

        // 2) Wrong guess
        lastMessage = null;
        wc.attemptRiddle("foo");
        assertEquals(start, p.getLocation());
        assertNotNull(lastMessage);
        assertTrue(lastMessage.contains("Wrong!"));

        // 3) Correct guess
        lastMessage = null;
        wc.attemptRiddle("key");
        assertEquals(puzzle, p.getLocation());
        assertNotNull(lastMessage);
        assertTrue(lastMessage.contains("Right!"));
    }
}
