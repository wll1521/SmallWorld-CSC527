package edu.southalabama.csc527.smallworld.textui.parser;

import edu.southalabama.csc527.smallworld.model.World;

/**
 * For user interfaces that rely on a text parser, this interface provides a
 * means for error messages and command output to come from the parser directly
 * to the UI. Some commands, such as 'help' and 'look', are executed entirely in
 * the UI (they may query the world, but don't affect it).
 * 
 */
public interface IParserObserver {
	
    /**
     * Displays a complete description of the player's current location to the
     * user
     * 
     * @param world The world containing the player and its location
     */
    void look(World world);

    /**
     * Displays a message to the user
     * 
     * @param msg The message to display
     */
    void display(String msg);
}
