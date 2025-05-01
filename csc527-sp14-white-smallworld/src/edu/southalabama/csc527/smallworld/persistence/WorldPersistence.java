package edu.southalabama.csc527.smallworld.persistence;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.southalabama.csc527.smallworld.model.*;

/**
 * The persistence capability for the game. This class defines static methods to
 * load and store the game state as an XML file. It uses the JDOM library to
 * manipulate XML trees.
 * <p>
 * Persistence is separate architecture layer from the game model. Specifically,
 * this class depends upon the model but the model <i>never</i> depends upon
 * this package.
 * 
 */
public class WorldPersistence {

	/**
	 * The version of the game as defined by the XML save file format.
	 */
	public static final String SAVEFILE_VERSION = "1.1";

	/**
	 * The full location, on the Java classpath, of the default world file.
	 */
	public static final String DEFAULT_WORLD = "/edu/southalabama/csc527/smallworld/persistence/space_adventure.xml";

	/**
	 * Loads the game state from the specified filename on the Java classpath of
	 * the running program and creates a usable World instance.
	 * 
	 * @param classpathLocation
	 *            the non-null string representing the full location, on the
	 *            Java classpath, of the desired world file.
	 * @return a game world.
	 */
	public static World loadWorld(String classpathLocation) {
		URL defaultURL = WorldPersistence.class.getResource(classpathLocation);
		if (defaultURL == null) {
			throw new IllegalStateException(
					"Unable to find world file:  cannot locate \""
							+ classpathLocation + "\" in classpath");
		}
		try {
			InputStream in = defaultURL.openStream();
			return loadWorld(in);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to open world file", e);
		}
	}

	/**
	 * Loads the game state from the specified {@link File} and creates a usable
	 * World instance. This is a convenience method that simply opens and
	 * {@link java.io.InputStream} on the specified {@link File} and calls
	 * {@link #loadWorld(InputStream)}.
	 * 
	 * @param file
	 *            the non-null file to read the game state from.
	 * @return a game world.
	 */
	public static World loadWorld(File file) {
		try {
			return loadWorld(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Unable to find world file", e);
		}
	}

	/**
	 * Loads the game state from the specified {@link java.io.InputStream} and
	 * creates a {@link World} usable World instance.
	 * 
	 * @param in
	 *            the non-null stream to read the game state from.
	 * @return a game world.
	 */
	public static World loadWorld(InputStream in) {
		assert (in != null);

		World world = new World();
		SAXBuilder parser = new SAXBuilder();
		try {
			Document saveXML = parser.build(in);
			Element root = saveXML.getRootElement();

			loadPlaceXML(root, world);

			loadPlayerXML(root, world);
			
			loadItemXML(root, world);


		} catch (IOException e) {
			throw new IllegalStateException(
					"A system error occurred while reading world file:", e);
		} catch (JDOMException e) {
			throw new IllegalStateException("Errors found in world file:", e);
		}
		return world;
	}

	/**
	 * Saves the state of the specified {@link World} into the specified
	 * {@link File} in XML format. It is suggested calls to this surround the
	 * call with a try-catch block if recovery from a save problem is desired.
	 * 
	 * @param world
	 *            the game state to save.
	 * @param file
	 *            the file to save the game state to.
	 * @throws IllegalStateException
	 *             if something goes wrong.
	 */
	public static void saveWorld(World world, File file) {
		Element worldElement = new Element(SMALLWORLD_TAG);

		worldElement.setAttribute(VERSION_TAG, SAVEFILE_VERSION);

		/*
		 * Create XML for Places
		 */
		for (Place l : world.getPlaces()) {
			/*
			 * We don't save the nowhere place to the save file. This place
			 * always exists in every world so its inclusion in the save file
			 * XML will cause an attempt on loading a save file into a model to
			 * try and create it again (resulting in an exception).
			 */
			if (l != world.getNowherePlace()) {
				worldElement.addContent(createPlaceXML(l));
			}
		}
		
		// Save items for each place
		for (Place place : world.getPlaces()) {
		    for (Item item : place.getItems()) {
		        worldElement.addContent(createItemXML(item, place));
		    }
		}
		
		// Save inventory
	    for (Item item : world.getPlayer().getInventory()) {
	        Element invItem = new Element("item");
	        invItem.setAttribute("name",      item.getName());
	        invItem.setAttribute("article",   item.getArticle());
	        invItem.setAttribute("location",  "PLAYER");
	        invItem.setAttribute("takePoints", Integer.toString(item.getTakePoints()));
	        invItem.setAttribute("dropPoints", Integer.toString(item.getDropPoints()));
	     // persist all the rules so loadItemXML can re-register them
	              for (Map.Entry<String, ItemLocationRule> entry : item.getLocationRules().entrySet()) {
	                  String ruleLoc = entry.getKey();
	                  ItemLocationRule rule = entry.getValue();
	                  Element ruleEl = new Element("location");
	                  ruleEl.setText(ruleLoc);
	                  if (rule.isNeededToEnter()) ruleEl.setAttribute("neededToEnter","Y");
	                  if (rule.getBlockedMsg() != null)
	                      ruleEl.setAttribute("blockedMsg", rule.getBlockedMsg());
	                  if (rule.getTakePoints() != 0)
	                      ruleEl.setAttribute("takePoints",
	                                          Integer.toString(rule.getTakePoints()));
	                  if (rule.getDropPoints() != 0)
	                      ruleEl.setAttribute("dropPoints",
	                                          Integer.toString(rule.getDropPoints()));
	                  invItem.addContent(ruleEl);
	              }
	              worldElement.addContent(invItem);
	           }


		/*
		 * Create XML for the Player
		 */
		worldElement.addContent(createPlayerXML(world.getPlayer()));

		Document gameStateInformation = new Document(worldElement);

		try {
			OutputStream save = new BufferedOutputStream(new FileOutputStream(
					file));
			// XML outputter with two-space indentation and newlines after
			// elements
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			// actually output the XML tree to the save file
			outputter.output(gameStateInformation, save);
			save.close();
		} catch (IOException e) {
			// something went wrong
			throw new IllegalStateException("Unable to write world file", e);
		}
	}

	/**
	 * Creates an XML tree for a game place.
	 * 
	 * @param place
	 *            the game place.
	 * @return the constructed XML tree.
	 */
	private static Element createPlaceXML(Place place) {
		Element placeElement = new Element(PLACE_TAG);
		placeElement.setAttribute(NAME_TAG, place.getName());
		placeElement.setAttribute(ARTICLE_TAG, place.getArticle());
		Element description = new Element(DESCRIPTION_TAG);
		placeElement.addContent(description);
		description.setText(place.getDescription());
		
		// added win condition attribute
		if (place.arrivalWinsGame()) {
			placeElement.setAttribute(WINS_TAG, "Y");
		}
		
		// get any riddles
		Riddle r = place.getWorld().getRiddleManager().getRiddleFor(place.getName());
	    if (r != null) {
	        Element rEl = new Element("riddle");
	        rEl.setText(r.getPrompt());
	        rEl.setAttribute("answer", r.getAnswer());
	        rEl.setAttribute("failMsg", r.getFailMsg());
	        rEl.setAttribute("successMsg", r.getSuccessMsg());
	        if (r.isSolved()) {
	            rEl.setAttribute("solved", "Y");
	        }
	        placeElement.addContent(rEl);
	    }

		for (Direction possibleDirection : Direction.values()) {
			if (place.isTravelAllowedToward(possibleDirection)) {
				Element neighbor = new Element(TRAVEL_TAG);
				placeElement.addContent(neighbor);
				neighbor.setAttribute(DIRECTION_TAG, possibleDirection
						.getAbbreviation());
				neighbor.setText(place.getTravelDestinationToward(
						possibleDirection).getName());
			}
		}

		return placeElement;
	}

	/**
	 * Creates an XML tree for the player.
	 * 
	 * @param player
	 *            the player.
	 * @return the constructed XML tree.
	 */
	private static Element createPlayerXML(Player player) {
		Element playerElement = new Element(PLAYER_TAG);
		playerElement.setAttribute(LOCATION_TAG, ""
				+ player.getLocation().getName());
		// persistent player score
		playerElement.setAttribute("score", Integer.toString(player.getPoints()));
		return playerElement;
	}

	/**
	 * Loads all places found within the root XML element into the world under
	 * construction.
	 * 
	 * @param root
	 *            the root of the save file's XML tree.
	 * @param world
	 *            the game world under construction.
	 */
	@SuppressWarnings("unchecked")
	private static void loadPlaceXML(Element root, World world) {
		List<Element> placeList = root.getChildren(PLACE_TAG);
		for (Element placeElement : placeList) {
			String name = placeElement.getAttributeValue(NAME_TAG);
			String article = placeElement.getAttributeValue(ARTICLE_TAG);
			String description = placeElement.getChild(DESCRIPTION_TAG)
					.getText();
			if (name == null || article == null || description == null)
				throw new IllegalStateException();
			
			// attribute tag and boolean condition added
			String win = placeElement.getAttributeValue(WINS_TAG);
			boolean arrivalWins = (win != null && win.contains("Y"));
			
			// Create new place and winning place if set in xml
			Place createNewPlace = world.createPlace(name, article, description);
			if (arrivalWins) {
				createNewPlace.setArrivalWinsGame(true);
			}
			
			// gets xml riddle tag element attributes
			Element rEl = placeElement.getChild("riddle");
			if (rEl != null) {
			    String prompt     = rEl.getTextTrim();
			    String answer     = rEl.getAttributeValue("answer");
			    String failMsg    = rEl.getAttributeValue("failMsg");
			    String successMsg = rEl.getAttributeValue("successMsg");
			    Riddle riddle     = new Riddle(prompt, answer, failMsg, successMsg);
			    // restore solved‐state if it was saved
	            if ("Y".equalsIgnoreCase(rEl.getAttributeValue("solved"))) {
	                riddle.attempt(answer);
	            }
			    world.getRiddleManager().addRiddle(name, riddle);	// register for worldcontroller / parser
			}
			
			//world.createPlace(name, article, description);
		}
		/*
		 * Second Pass: Next, we need to connect the places into a map as
		 * specified by the "travel" elements in the XML.
		 */
		for (Element placeElement : placeList) {
			Place l = world.getPlace(placeElement.getAttributeValue(NAME_TAG));
			if (l == null)
				throw new IllegalStateException(
						"Unable to find a place named \""
								+ placeElement.getAttributeValue(NAME_TAG)
								+ "\" during the second pass through the file..."
								+ "did the file change while we were reading it?");
			
			List<Element> travelList = placeElement.getChildren(TRAVEL_TAG);
			for (Element t : travelList) {
				Direction d = Direction.getInstance(t
						.getAttributeValue(DIRECTION_TAG));
				if (d == null)
					throw new IllegalStateException("\""
							+ t.getAttributeValue(DIRECTION_TAG)
							+ "\" is not a valid direction for travel from "
							+ "the place named \"" + l.getName() + "\"");

				Place destPlace = world.getPlace(t.getText());
				if (destPlace == null)
					throw new IllegalStateException(
							"Unable to find a place named \"" + t.getText()
									+ "\" as the destination when traveling "
									+ d + " from the place named \""
									+ l.getName() + "\"");

				l.setTravelDestination(d, destPlace);
			}
		}
	}

	/**
	 * Loads information about the player found within the root XML element into
	 * the world under construction.
	 * 
	 * @param root
	 *            the root of the save file's XML tree.
	 * @param world
	 *            the game world under construction.
	 */
	private static void loadPlayerXML(Element root, World world) {
		Element playerElement = root.getChild(PLAYER_TAG);
		if (playerElement == null)
			throw new IllegalStateException();

		String locationName = playerElement.getAttributeValue(LOCATION_TAG);
		if (locationName != null) {
			Place location = world.getPlace(locationName);
			if (location != null) {
				world.getPlayer().setLocation(location);
			} else {
				System.err.println("Unable to find a place named \""
						+ locationName + "\" as the player's location");
			}
		}
			// restore score
		String scoreStr = playerElement.getAttributeValue("score");
		   if (scoreStr != null) {
		           world.getPlayer().addPoints(Integer.parseInt(scoreStr));  
		   }
		}
	
	//Items additions aside from load and save changes: creates item instance from xml
	@SuppressWarnings("unchecked") //override to fix Eclipse error
	private static void loadItemXML(Element root, World world) {
	    // getChildren now returns raw list
	    List itemList = root.getChildren("item");
	    for (Object itemObj : itemList) {
	        Element itemElement = (Element) itemObj;

	        String name    = itemElement.getAttributeValue("name");
	        String article = itemElement.getAttributeValue("article");
	        String locName = itemElement.getAttributeValue("location");
	        int takePoints = parseIntSafe(itemElement.getAttributeValue("takePoints"));
	        int dropPoints = parseIntSafe(itemElement.getAttributeValue("dropPoints"));

	        Item newItem = new Item(name, article, takePoints, dropPoints);

	        // Put in inventory rather than place
	        if ("PLAYER".equalsIgnoreCase(locName)) {
	            world.getPlayer().getInventory().add(newItem);
	        } else {
	            Place place = world.getPlace(locName);
	            if (place != null) {
	                place.addItem(newItem);
	            } else {
	                world.addToMessage("Warning: Unknown location \""
	                    + locName + "\" for item " + newItem.getShortDescription());
	            }
	        }

	        // parse location rules
	        List ruleElements = itemElement.getChildren("location");
	        for (Object ruleObj : ruleElements) {
	            Element ruleEl = (Element) ruleObj;
	            String ruleLoc     = ruleEl.getTextTrim();
	            boolean needed     = "Y".equalsIgnoreCase(ruleEl.getAttributeValue("neededToEnter"));
	            String blockedMsg  = ruleEl.getAttributeValue("blockedMsg");
	            int bonusTake      = parseIntSafe(ruleEl.getAttributeValue("takePoints"));
	            int bonusDrop      = parseIntSafe(ruleEl.getAttributeValue("dropPoints"));

	            ItemLocationRule rule = new ItemLocationRule(needed, blockedMsg, bonusTake, bonusDrop);
	            newItem.addLocationRule(ruleLoc, rule);
	            if (needed) {
	                world.addRequiredItem(ruleLoc, newItem);
	            }
	        }
	    }
	}

	// helper try function
	private static int parseIntSafe(String s) {
	    try {
	        return Integer.parseInt(s);
	    } catch (Exception e) {
	        return 0;
	    }
	}


	// create an <item> element with its associated attributes
	private static Element createItemXML(Item item, Place location) {
	    Element itemElement = new Element("item");
	    itemElement.setAttribute("name", item.getName());
	    itemElement.setAttribute("article", item.getArticle());
	    itemElement.setAttribute("location", location.getName());
	    itemElement.setAttribute("takePoints", Integer.toString(item.getTakePoints()));
	    itemElement.setAttribute("dropPoints", Integer.toString(item.getDropPoints()));
	 // Adds persistence to place item rules
	    for (Map.Entry<String,ItemLocationRule> entry : item.getLocationRules().entrySet()) {
	        String locName = entry.getKey();
	        ItemLocationRule rule = entry.getValue();
	        Element ruleEl = new Element("location");
	        ruleEl.setText(locName);
	        if (rule.isNeededToEnter()) {
	        	ruleEl.setAttribute("neededToEnter","Y");
	        }
	        if (rule.getBlockedMsg() != null)
	            ruleEl.setAttribute("blockedMsg", rule.getBlockedMsg());
	        if (rule.getTakePoints() != 0)
	            ruleEl.setAttribute("takePoints", Integer.toString(rule.getTakePoints()));
	        if (rule.getDropPoints() != 0)
	            ruleEl.setAttribute("dropPoints", Integer.toString(rule.getDropPoints()));
	        itemElement.addContent(ruleEl);
	    }
	    return itemElement;
	}
	



	private static final String ARTICLE_TAG = "article";

	private static final String DESCRIPTION_TAG = "description";

	private static final String DIRECTION_TAG = "direction";

	private static final String PLACE_TAG = "place";

	private static final String LOCATION_TAG = "location";

	private static final String NAME_TAG = "name";

	private static final String PLAYER_TAG = "player";

	private static final String SMALLWORLD_TAG = "smallworld";

	private static final String TRAVEL_TAG = "travel";

	private static final String VERSION_TAG = "version";
	
	private static final String WINS_TAG = "arrivalWinsGame"; //added win tag
}
