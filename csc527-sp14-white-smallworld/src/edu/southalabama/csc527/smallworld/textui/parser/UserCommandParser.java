package edu.southalabama.csc527.smallworld.textui.parser;

import edu.southalabama.csc527.smallworld.controller.WorldController;
import edu.southalabama.csc527.smallworld.model.Direction;
import static edu.southalabama.csc527.smallworld.textui.TextUtilities.*;
import edu.southalabama.csc527.smallworld.model.Item;

/**
 * Parses user commands and informs a specified {@link WorldController} instance
 * of its results. For example, the String "go north" causes the
 * {@link WorldController#travel(Direction)} method to be invoked.
 * 
 */
public final class UserCommandParser {

    /**
     * The non-null world controller associated with this parser.
     */
    private final WorldController f_wc;

    /**
     * An observer of this parser, recipient of certain UI-only
     * commands and error messages.
     */
    private IParserObserver f_pwo;

    /**
     * Constructs a text command parser with the specified observer and
     * WorldController.  The controller is sent commands that affect the
     * World (or could), while the observer is sent error messages and
     * output from commands that only query the world, in particular
     * those expected to be unique to a text-based interface.
     * 
     * @param controller a non-null controller for this parser to invoke once it
     *            understands the user's game command.
     * @param observer a non-null observer for this parser to send messages and
     *            query command output to
     */
    public UserCommandParser(WorldController controller,
                             IParserObserver observer) {
        assert (controller != null && observer != null);

        f_wc = controller;
        f_pwo = observer;
    }

    /**
     * Parses the user's command and sending appropriate messages to the
     * {@link WorldController}. Once the users command is understood (i.e.,
     * parsed) a specific method is invoked on the controller. If the parser
     * couldn't understand the user's command an error message is sent.
     * 
     * @param command the users's command to the game.
     */
    public void parse(String command) {
    	String[] words = command.trim().toUpperCase().split("\\s+");

        /*
         * The below flag is used to indicate if we were able to understand the
         * command and callback our observer (f_observer) to execute a command
         * in the game.
         */
        boolean commandExecuted = false;
        
        // riddle first check
        // if we’re in the middle of a riddle, treat the entire line as the guess
        // rather than an action command. Otherwise go through normal parsing
        if (f_wc.hasPendingRiddle()) {
            String up = command.trim().toUpperCase();

            // Allow look command
            if (words[0].equals("LOOK")) {
                f_pwo.look(f_wc.getWorld());
                return;
            }

            // Now compute travel logic otherwise, solve riddle
            Direction dir = null;
            if (words.length == 1) {
                dir = Direction.getInstance(words[0]);
            } else if (words[0].equals("GO") && words.length > 1) {
                dir = Direction.getInstance(words[1]);
            }

            if (dir != null) {
                // For moving away
                f_wc.clearPendingRiddle();
                f_wc.travel(dir);
                return;
            } 
            
            // If it's not a move or look command then it is a guess
            f_wc.attemptRiddle(command);
            return;
        }
        
        // If command is a single word and valid direction treat as movement command
        if (words.length == 1 && Direction.getInstance(words[0]) != null) {
        	f_wc.travel(Direction.getInstance(words[0]));
        	commandExecuted = true;
        }

        if (words[0].equals("GO") || words[0].equals("MOVE")) {
            /*
             * "GO <direction>" command
             */
            Direction direction = null;
            if (words.length == 1) {
                f_pwo.display(
                        words[0] + " where?  You must specify a direction!"
                        + " Please type \"help\" if you need more help.");
            } else {
                direction = Direction.getInstance(words[1]);
                if (direction == null) {
                    f_pwo.display(
                        words[1] + " is not a direction I recognize. "
                        + "Please type \"help\" if you need more help.");
                } else {
                    f_wc.travel(direction);
                }
            }
            commandExecuted = true;

        } else if (words[0].equals("HELP")) {
            /*
             * "HELP" command
             */
            f_pwo.display(getHelpMessage());
            commandExecuted = true;

        } else if (words[0].equals("LOAD")) {
            /*
             * "LOAD <file>" command
             */
            String fileName = command.substring(words[0].length()).trim();
            if (fileName.length() == 0) {
                f_pwo.display(
                        "You must specify a file name to load a saved game.  "
                        + "Please type \"help\" if you need more help.");
            } else {
                f_wc.loadWorld(fileName);
            }
            commandExecuted = true;

        } else if (words[0].equals("LOOK")) {
            /*
             * "LOOK" command
             * This is executed locally and the result sent through
             * the Controller's World back to its observers.
             */
            f_pwo.look(f_wc.getWorld());
            commandExecuted = true;

        } else if (words[0].equals("QUIT") || words[0].equals("EXIT")) {
            /*
             * "QUIT" or "EXIT" command
             */
            f_wc.quit();
            commandExecuted = true;

        } else if (words[0].equals("SAVE")) {
            /*
             * "SAVE <file>" command
             */
            String fileName = command.substring(words[0].length()).trim();
            if (fileName.length() == 0) {
                f_pwo.display(
                        "You must specify a file name to save your game.  "
                        + "Please type \"help\" if you need more help.");
            } else {
                f_wc.saveWorld(fileName);
            }
            commandExecuted = true;
        }
        
     // New Item related commands (help also added):  
     // New command: TAKE (either a specific item or "take all")
        if (words[0].equals("TAKE")) {
            if (words.length == 1 || command.trim().equalsIgnoreCase("TAKE ALL")) {
                f_wc.takeAll();
            } else {
                // Extract the item name from the command. Item is everything after "TAKE "
                String itemName = command.substring(4).trim();
                // Helper function in World to find an item in the current location by name
                Item item = f_wc.getWorld().findItemInCurrentLocation(itemName);
                if (item != null) {
                    f_wc.take(item);
                } else {
                    f_pwo.display("Item \"" + itemName + "\" not found at your current location.");
                }
            }
            commandExecuted = true;
        }
        
        // New command: DROP
        else if (words[0].equals("DROP")) {
            if (words.length == 1) {
                f_pwo.display("Please specify which item to drop.");
            } else {
                String itemName = command.substring(5).trim(); // remove "DROP " from command
                // Helper function in World to search for an item in the player's inventory by name
                Item item = f_wc.getWorld().findItemInInventory(itemName);
                if (item != null) {
                    f_wc.drop(item);
                } else {
                    f_pwo.display("You are not carrying \"" + itemName + "\".");
                }
            }
            commandExecuted = true;
        }
        
        // New command: INVENTORY ("INV" or "I")
        else if (words[0].equals("INVENTORY") || words[0].equals("INV") || words[0].equals("I")) {
            f_wc.inventory();
            commandExecuted = true;
        }

        if (!commandExecuted) {
            /*
             * We couldn't understand the command the user entered. Hence we
             * need report this problem.
             */
            f_pwo.display("Sorry, I don't understand what the command \""
                            + command
                            + "\" means. Please type \"help\" if you need some help.");
        }
    }

    /**
     * Constructs a message containing some (hopefully useful) help
     * to the user about what the game commands do.
     */
    private String getHelpMessage() {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage
                .append("You are controlling a player "
                        + "interacting with a small world created within the computer.  "
                        + "The commands you type control what the player does within "
                        + "the game.  Some actions add points to your score.  You win the "
                        + "game when you obtain enough points through your actions.");
        helpMessage.append(LINESEP2);
        helpMessage
                .append("The following is a description of the commands the "
                        + "game understands:");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"go <north | south | east | west>\" moves the "
                + "player from his or her current location in the specified "
                + "direction to a new location. The word \"move\" may be used "
                + "to mean \"go\" within this command. "
                + "An example is \"go north\" or \"go n\"");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"load <filename>\" loads the specified save file "
                + "into the game.  Discards the existing game state."
                + "  Example \"load C:\\save1.xml\"");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"look\" examines the player's current location.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"quit\" or \"exit\" terminates the game.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"save <filename>\" saves the current game state "
                + "to the specified filename. Example \"save C:\\save1.xml\"");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"take <item name>\" to pick up an item at your current location. Use \"take all\" to pick up everything.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"drop <item name>\" to drop an item from your inventory.");
        helpMessage.append(LINESEP2);
        helpMessage.append("\"inventory\" (or \"inv\" or \"I\") to list the items you are carrying.");
        return helpMessage.toString();
    }
}
