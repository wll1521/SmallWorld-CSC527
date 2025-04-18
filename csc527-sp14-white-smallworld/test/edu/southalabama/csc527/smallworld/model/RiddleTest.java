package edu.southalabama.csc527.smallworld.model;

import junit.framework.TestCase;


public class RiddleTest extends TestCase {

    public void testBasicSolveFlow() {
        Riddle r = new Riddle(
            "What is 2+2?",    // prompt
            "4",               // answer
            "Wrong!",          // failMsg
            "Correct!"         // successMsg
        );                                         
        assertEquals("What is 2+2?", r.getPrompt());
        assertFalse(r.isSolved());
        
        // wrong guess
        assertFalse(r.attempt("3"));
        assertFalse(r.isSolved());
        assertEquals("Wrong!", r.getFailMsg());
        
        // correct guess
        assertTrue(r.attempt("4"));
        assertTrue(r.isSolved());
        assertEquals("Correct!", r.getSuccessMsg());
        
        // once solved, further attempts always true
        assertTrue(r.attempt("anything"));
    }
}
