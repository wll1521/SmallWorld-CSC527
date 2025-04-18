package edu.southalabama.csc527.smallworld.persistence;

import junit.framework.TestCase;
import edu.southalabama.csc527.smallworld.TestConstants;
import edu.southalabama.csc527.smallworld.model.World;
import edu.southalabama.csc527.smallworld.model.Riddle;
import edu.southalabama.csc527.smallworld.model.RiddleManager;


 //Verify that WorldPersistence loads <riddle> tags into the RiddleManager

public class PersistenceRiddleLoadTest extends TestCase {

    public void testRiddleLoadedFromXML() {
        World w = WorldPersistence.loadWorld(TestConstants.TEST_RIDDLE_FILE);
        RiddleManager rm = w.getRiddleManager();  
        Riddle r = rm.getRiddleFor("PuzzleRoom");
        assertNotNull(r);
        
        // prompt and messages
        assertEquals("What opens locks?", r.getPrompt());
        assertEquals("Wrong!", r.getFailMsg());
        assertEquals("Right!", r.getSuccessMsg());
        assertFalse(r.isSolved());
        
        // attempt wrong then right
        assertFalse(r.attempt("nope"));
        assertTrue(!r.isSolved());
        assertTrue(r.attempt("key"));
        assertTrue(r.isSolved());
    }
}
