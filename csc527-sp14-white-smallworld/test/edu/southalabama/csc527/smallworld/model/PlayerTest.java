package edu.southalabama.csc527.smallworld.model;

import edu.southalabama.csc527.smallworld.model.Place;
import edu.southalabama.csc527.smallworld.model.Player;
import edu.southalabama.csc527.smallworld.model.World;
import junit.framework.TestCase;

/**
 * @see Player
 */
public class PlayerTest extends TestCase {

	private World f_world;

	private Player f_player;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		f_world = new World();
		f_world.createPlace("city of Dayton", "the",
				"You are in the glamorous mid-west city of Dayton");
		f_player = f_world.getPlayer();
	}

	public void testPlayer() {
		try {
			new Player(null);
			fail();
		} catch (AssertionError e) {
		}
	}

	/*
	 * Test method for 'edu.southalabama.csc527.smallworld.model.Player.getWorld()'
	 */
	public void testGetWorld() {
		assertEquals(f_world, f_player.getWorld());
	}

	/*
	 * Test method for 'edu.southalabama.csc527.smallworld.model.Player.getName()'
	 */
	public void testGetName() {
		assertEquals("Player", f_player.getName());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Player.getShortDescription()'
	 */
	public void testGetShortDescription() {
		assertEquals("the Player", f_player.getShortDescription());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Player.getDescription()'
	 */
	public void testGetDescription() {
		assertEquals("Our Hero", f_player.getDescription());
	}

	/*
	 * Test method for 'edu.southalabama.csc527.smallworld.model.Player.getLocation()'
	 */
	public void testGetLocation() {
		assertEquals(f_world.getNowherePlace(), f_player.getLocation());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Player.setLocation(Place)'
	 */
	public void testSetLocation() {
		Place location = f_world.getPlace("city of Dayton");
		f_player.setLocation(location);
		assertEquals(location, f_player.getLocation());

		try {
			f_player.setLocation(null);
			fail();
		} catch (AssertionError e) {
		}
	}

}
