package edu.southalabama.csc527.smallworld.model;

public class Riddle {
    private final String prompt; 		// what is shown to the player
    private final String answer; 		// correct answer
    private final String failMsg; 		// shown on a wrong guess
    private final String successMsg;	// shown on a correct guess
    private boolean solved = false;		// tracks if a riddle has been solved already

    public Riddle(String prompt, String answer, String failMsg, String successMsg) {
        this.prompt      = prompt;
        this.answer      = answer;
        this.failMsg     = failMsg;
        this.successMsg  = successMsg;
    }

    public String getPrompt() { 
    	return prompt; 
    }
    
    public boolean isSolved() { 
    	return solved; 
    }
    
    public boolean attempt(String guess) {
        if (solved) return true;
        if (guess.equalsIgnoreCase(answer)) {
            solved = true;
            return true;
        }
        return false;
    }
    
    public String getFailMsg() { 
    	return failMsg; 
    }
    
    public String getSuccessMsg() { 
    	return successMsg; 
    }
}
