package edu.southalabama.csc527.smallworld.model;

import edu.southalabama.csc527.smallworld.model.Direction;
import edu.southalabama.csc527.smallworld.model.Place;
import edu.southalabama.csc527.smallworld.model.World;
import junit.framework.TestCase;

/**
 * @see Place
 */
public class PlaceTest extends TestCase {

	private World f_world;

	private Place f_Dayton, f_Columbus, f_nowhere;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		f_world = new World();
		f_nowhere = f_world.getNowherePlace();
		f_Dayton = new Place(f_world, "city of Dayton", "the",
				"You are in the charming Midwest town of Dayton, Ohio.");
		f_Columbus = new Place(f_world, "city of Columbus", "the",
				"You are in Columbus, the capital of Ohio");
		f_Dayton.setTravelDestination(Direction.EAST, f_Columbus);
		f_Columbus.setTravelDestination(Direction.WEST, f_Dayton);
	}

	public void testPlace() {
		try {
			new Place(null, "somewhere", "", "you are somewhere");
			fail();
		} catch (AssertionError e) {
		}
		try {
			new Place(f_world, null, "", "you are somewhere");
			fail();
		} catch (AssertionError e) {
		}
		try {
			new Place(f_world, "somewhere", null, "you are somewhere");
			fail();
		} catch (AssertionError e) {
		}
		try {
			new Place(f_world, "somewhere", "", null);
			fail();
		} catch (AssertionError e) {
		}
	}

	/*
	 * Test method for 'edu.southalabama.csc527.smallworld.model.Place.getWorld()'
	 */
	public void testGetWorld() {
		assertEquals(f_world, f_nowhere.getWorld());
		assertEquals(f_world, f_Dayton.getWorld());
	}

	/*
	 * Test method for 'edu.southalabama.csc527.smallworld.model.Place.getName()'
	 */
	public void testGetName() {
		assertEquals("Very Remote Place", f_nowhere.getName());
		assertEquals("city of Dayton", f_Dayton.getName());
	}

	/*
	 * Test method for 'edu.southalabama.csc527.smallworld.model.Place.getArticle()'
	 */
	public void testGetArticle() {
		assertEquals("a", f_nowhere.getArticle());
		assertEquals("the", f_Dayton.getArticle());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Place.getShortDescription()'
	 */
	public void testGetShortDescription() {
		assertEquals("a Very Remote Place", f_nowhere.getShortDescription());
		assertEquals("the city of Dayton", f_Dayton.getShortDescription());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Place.getDescription()'
	 */
	public void testGetDescription() {
		assertEquals("You are in a very remote place.", f_nowhere
				.getDescription());
		assertEquals("You are in the charming Midwest town of Dayton, Ohio.",
				f_Dayton.getDescription());
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Place.isTravelAllowedToward(Direction)'
	 */
	public void testIsTravelAllowedToward() {
		for (Direction d : Direction.values())
			assertFalse(f_nowhere.isTravelAllowedToward(d));
		for (Direction d : Direction.values())
			if (d == Direction.EAST)
				assertTrue(f_Dayton.isTravelAllowedToward(d));
			else
				assertFalse(f_Dayton.isTravelAllowedToward(d));
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Place.getTravelDestinationToward(Direction)'
	 */
	public void testGetTravelDestinationToward() {
		for (Direction d : Direction.values())
			assertNull(f_nowhere.getTravelDestinationToward(d));
		for (Direction d : Direction.values())
			if (d == Direction.EAST)
				assertEquals(f_Dayton.getTravelDestinationToward(d), f_Columbus);
			else
				assertNull(f_Dayton.getTravelDestinationToward(d));
	}

	/*
	 * Test method for
	 * 'edu.southalabama.csc527.smallworld.model.Place.setTravelDestination(Direction,
	 * Place)'
	 */
	public void testSetTravelDestination() {
		f_Columbus.setTravelDestination(Direction.EAST, f_nowhere);
		assertEquals(f_Columbus.getTravelDestinationToward(Direction.EAST),
				f_nowhere);
		try {
			f_Columbus.setTravelDestination(null, f_Dayton);
			fail();
		} catch (AssertionError e) {
		}
		try {
			f_Columbus.setTravelDestination(Direction.NORTH, null);
			fail();
		} catch (AssertionError e) {
		}
	}

}
