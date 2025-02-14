package edu.southalabama.csc527.smallworld.model;

import edu.southalabama.csc527.smallworld.model.Direction;
import junit.framework.TestCase;

/**
 * @see Direction
 */
public class DirectionTest extends TestCase {

	public void testGetInstance() {
		assertNull(Direction.getInstance(null));
		assertNull(Direction.getInstance("NorT"));
		assertNull(Direction.getInstance("FooBar"));

		assertEquals(Direction.NORTH, Direction.getInstance("N"));
		assertEquals(Direction.NORTH, Direction.getInstance("n"));
		assertEquals(Direction.NORTH, Direction.getInstance("NORTH"));
		assertEquals(Direction.NORTH, Direction.getInstance("north"));

		assertEquals(Direction.SOUTH, Direction.getInstance("S"));
		assertEquals(Direction.SOUTH, Direction.getInstance("s"));
		assertEquals(Direction.SOUTH, Direction.getInstance("SOUTH"));
		assertEquals(Direction.SOUTH, Direction.getInstance("south"));

		assertEquals(Direction.EAST, Direction.getInstance("E"));
		assertEquals(Direction.EAST, Direction.getInstance("e"));
		assertEquals(Direction.EAST, Direction.getInstance("EAST"));
		assertEquals(Direction.EAST, Direction.getInstance("east"));

		assertEquals(Direction.WEST, Direction.getInstance("W"));
		assertEquals(Direction.WEST, Direction.getInstance("w"));
		assertEquals(Direction.WEST, Direction.getInstance("WEST"));
		assertEquals(Direction.WEST, Direction.getInstance("west"));
	}

	public void testGetAbbreviation() {
		assertTrue(Direction.NORTH.getAbbreviation().equals("N"));
		assertTrue(Direction.SOUTH.getAbbreviation().equals("S"));
		assertTrue(Direction.EAST.getAbbreviation().equals("E"));
		assertTrue(Direction.WEST.getAbbreviation().equals("W"));
	}
}
